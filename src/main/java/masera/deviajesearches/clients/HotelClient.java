package masera.deviajesearches.clients;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masera.deviajesearches.dtos.amadeus.request.HotelOffersRequest;
import masera.deviajesearches.dtos.amadeus.request.HotelRequest;
import masera.deviajesearches.utils.AmadeusErrorHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;


/**
 * Cliente para consumir el microservicio de hoteles de la API de Amadeus.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class HotelClient {

  private final WebClient webClient;
  private final AmadeusErrorHandler errorHandler;

  private static final String HOTELS_BY_CITY_PATH = "/v1/reference-data/locations/hotels/by-city";
  private static final String HOTEL_OFFERS_PATH = "/v3/shopping/hotel-offers";
  private static final String HOTEL_OFFER_DETAILS_PATH = "/v3/shopping/hotel-offers/{offerId}";

  /**
   * Obtiene una lista de hoteles por ciudad.
   *
   * @param hotelRequest parámetros de búsqueda de hoteles.
   * @param token token de autenticación.
   * @return una lista de hoteles.
   */
  public Mono<Object> findsHotelsByCity(HotelRequest hotelRequest, String token) {

    log.info("Buscando hoteles en la ciudad: {}", hotelRequest.getCityCode());

    String uri = getUrl(hotelRequest);

    return webClient.get()
            .uri(uri)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .retrieve()
            .bodyToMono(Object.class)
            .doOnSuccess(response -> log.info("Búsqueda de hoteles completada exitosamente"))
            .doOnError(error -> log.error("Error al buscar hoteles: {}", error.getMessage()))
            .onErrorResume(WebClientResponseException.class, e -> {
              throw errorHandler.handleAmadeusError(e);
            });

  }

  /**
   * Obtiene detalles de una oferta específica.
   *
   * @param offerId El ID de la oferta.
   * @param token El token de autenticación.
   * @return Un Mono que emite la respuesta con los detalles de la oferta.
   */
  public Mono<Object> getHotelOfferDetails(String offerId, String token) {
    log.info("Obteniendo detalles de la oferta: {}", offerId);

    return webClient.get()
            .uri(HOTEL_OFFER_DETAILS_PATH, offerId)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .retrieve()
            .bodyToMono(Object.class)
            .doOnSuccess(response -> log.info("Detalles de oferta obtenidos exitosamente"))
            .onErrorResume(WebClientResponseException.class, e -> {
              throw errorHandler.handleAmadeusError(e);
            });
  }

  /**
   * Obtiene las ofertas de los hoteles por ID.
   *
   * @param hotelOffersRequest representa los parámetros
   *                           de búsqueda de ofertas de hoteles.
   * @param token token de autenticación.
   * @return una lista de ofertas de hoteles.
   */
  public Mono<Object> findOffersByHotelsId(HotelOffersRequest hotelOffersRequest, String token) {
    log.info("Buscando ofertas para {} hoteles", hotelOffersRequest.getHotelIds().size());

    String uri = getUrl(hotelOffersRequest);

    return webClient.get()
            .uri(uri)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .retrieve()
            .bodyToMono(Object.class)
            .doOnSuccess(response -> log
                    .info("Búsqueda de ofertas de hoteles completada exitosamente"))
            .doOnError(error -> log
                    .error("Error al buscar ofertas de hoteles: {}", error.getMessage()))
            .onErrorResume(WebClientResponseException.class, e -> {
              throw errorHandler.handleAmadeusError(e);
            });

  }



  /**
   * Construye la URL para la búsqueda de hoteles.
   *
   * @param hotelRequest representa los parámetros de búsqueda de hoteles.
   * @return la URL construida.
   */
  private String getUrl(HotelRequest hotelRequest) {
    return UriComponentsBuilder.fromPath(HOTELS_BY_CITY_PATH)
        .queryParam("cityCode", hotelRequest.getCityCode())
        .queryParamIfPresent("radius", Optional.ofNullable(hotelRequest.getRadius()))
        .queryParamIfPresent("radiusUnit", Optional.ofNullable(hotelRequest.getRadiusUnit()))
        .queryParamIfPresent("ratings", Optional.ofNullable(hotelRequest.getRatings()))
        .queryParamIfPresent("amenities", Optional.ofNullable(hotelRequest.getAmenities()))
        .queryParamIfPresent("chainCodes", Optional.ofNullable(hotelRequest.getChainCodes()))
        .build().toUriString();
  }

  /**
   * Construye la URL para la búsqueda de ofertas de hoteles.
   *
   * @param hotelOffersRequest representa los parámetros de búsqueda de ofertas de hoteles.
   * @return la URL construida.
   */
  private String getUrl(HotelOffersRequest hotelOffersRequest) {
    return UriComponentsBuilder.fromPath(HOTEL_OFFERS_PATH)
            .queryParam("hotelIds", String.join(",", hotelOffersRequest.getHotelIds()))
            .queryParam("checkInDate", hotelOffersRequest.getCheckInDate())
            .queryParam("checkOutDate", hotelOffersRequest.getCheckOutDate())
            .queryParam("adults", hotelOffersRequest.getAdults())
            .queryParamIfPresent("roomQuantity",
                    Optional.ofNullable(hotelOffersRequest.getRoomQuantity()))
            .queryParamIfPresent("currency",
                    Optional.ofNullable(hotelOffersRequest.getCurrency()))
            .queryParamIfPresent("priceRange",
                    Optional.ofNullable(hotelOffersRequest.getPriceRange()))
            .queryParamIfPresent("boardType",
                    Optional.ofNullable(hotelOffersRequest.getBoardType()))
            .build().toUriString();
  }
}
