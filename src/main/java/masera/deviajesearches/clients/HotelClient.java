package masera.deviajesearches.clients;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masera.deviajesearches.configs.HotelbedsConfig;
import masera.deviajesearches.dtos.amadeus.request.HotelSearchRequest;
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
  private final HotelbedsConfig hotelbedsConfig;
  private final AmadeusErrorHandler errorHandler;

  private static final String AVAILABILITY_ENDPOINT = "/hotel-api/1.0/hotels";
  private static final String CONTENT_ENDPOINT = "/hotel-content-api/1.0/hotels";
  private static final String CHECK_RATES_ENDPOINT = "/hotel-api/1.0/checkrates";

  /*/**
   * Obtiene una lista de hoteles por ciudad.
   *
   * @param hotelRequest parámetros de búsqueda de hoteles.
   * @param token token de autenticación.
   * @return una lista de hoteles.

  public Mono<HotelbedsAvailabilityResponse> searchHotels(HotelbedsAvailabilityRequest request) {
    log.info("Buscando hoteles en Hotelbeds con destino: {}", request.getDestination().getCode());

    return webClient
            .post()
            .uri(hotelbedsConfig.getBaseUrl() + AVAILABILITY_ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON)
            .headers(this::addHotelbedsHeaders)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(HotelbedsAvailabilityResponse.class)
            .doOnSuccess(response -> log.info("Búsqueda de hoteles completada exitosamente"))
            .doOnError(error -> log.error("Error al buscar hoteles: {}", error.getMessage()))
            .onErrorResume(WebClientResponseException.class, e -> {
              log.error("Error de respuesta de Hotelbeds: {}", e.getResponseBodyAsString());
              return Mono.error(new HotelbedsApiException("Error al buscar hoteles: " + e.getMessage(), e));
            });
  }*/

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
   * @param hotelSearchRequest representa los parámetros
   *                           de búsqueda de ofertas de hoteles.
   * @param token token de autenticación.
   * @return una lista de ofertas de hoteles.
   */
  public Mono<Object> findOffersByHotelsId(HotelSearchRequest hotelSearchRequest, String token) {
    log.info("Buscando ofertas para {} hoteles", hotelSearchRequest.getHotelIds().size());

    String uri = getUrl(hotelSearchRequest);

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
   * @param hotelSearchRequest representa los parámetros de búsqueda de ofertas de hoteles.
   * @return la URL construida.
   */
  private String getUrl(HotelSearchRequest hotelSearchRequest) {
    return UriComponentsBuilder.fromPath(HOTEL_OFFERS_PATH)
            .queryParam("hotelIds", String.join(",", hotelSearchRequest.getHotelIds()))
            .queryParam("checkInDate", hotelSearchRequest.getCheckInDate())
            .queryParam("checkOutDate", hotelSearchRequest.getCheckOutDate())
            .queryParam("adults", hotelSearchRequest.getAdults())
            .queryParamIfPresent("roomQuantity",
                    Optional.ofNullable(hotelSearchRequest.getRoomQuantity()))
            .queryParamIfPresent("currency",
                    Optional.ofNullable(hotelSearchRequest.getCurrency()))
            .queryParamIfPresent("priceRange",
                    Optional.ofNullable(hotelSearchRequest.getPriceRange()))
            .queryParamIfPresent("boardType",
                    Optional.ofNullable(hotelSearchRequest.getBoardType()))
            .build().toUriString();
  }

  private void addHotelbedsHeaders(HttpHeaders headers) {
    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    String signature = DigestUtils.sha256Hex(hotelbedsConfig.getApiKey() + hotelbedsConfig.getSecret() + timestamp);

    headers.set("Api-Key", hotelbedsConfig.getApiKey());
    headers.set("X-Signature", signature);
    headers.set("Accept", "application/json");
    headers.set("Accept-Encoding", "gzip");
  }
}
