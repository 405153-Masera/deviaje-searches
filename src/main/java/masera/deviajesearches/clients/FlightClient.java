package masera.deviajesearches.clients;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masera.deviajesearches.configs.AmadeusConfig;
import masera.deviajesearches.dtos.amadeus.request.FlightSearchRequest;
import masera.deviajesearches.utils.AmadeusErrorHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;


/**
 * Cliente para realizar búsquedas a la API de vuelos.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class FlightClient {

  private final WebClient webClient;

  private final AmadeusErrorHandler errorHandler;

  private final AmadeusConfig amadeusConfig;

  private static final String FLIGHT_OFFERS_URL_V1 = "/v1/shopping/flight-offers";

  private static  final String FLIGHT_OFFERS_URL_V2 = "/v2/shopping/flight-offers";

  private static final String LOCATIONS_PATH = "/v1/reference-data/locations";

  /**
   * Realiza una búsqueda de ofertas de vuelos.
   *
   * @param flightSearchRequest representa los parámetros de búsqueda de vuelos.
   * @param token token de autenticación.
   * @return Mono Object que representa la respuesta de la búsqueda de vuelos.
   */
  public Mono<Object> searchFlightOffers(FlightSearchRequest flightSearchRequest, String token) {

    String uri = getUrl(flightSearchRequest);

    return webClient.get()
            .uri(uri)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .retrieve()
            .bodyToMono(Object.class)
            .doOnSuccess(response -> log.info("Búsqueda de vuelos completada exitosamente"))
            .doOnError(error -> log.error("Error al buscar vuelos: {}", error.getMessage()))
            .onErrorResume(WebClientResponseException.class, e -> {
              throw  errorHandler.handleAmadeusError(e);
            });
  }

  /**
   * Obtiene ofertas alternativas de un vuelo (upselling).
   *
   * @param request objeto que contiene la oferta de vuelo original
   * @param token token de autenticación
   * @return ofertas alternativas del vuelo
   */
  public Mono<Object> searchUpsellingFlightOffers(Object request, String token) {
    log.info("Solicitando ofertas alternativas para vuelo");

    String uri = amadeusConfig.getBaseUrl() + FLIGHT_OFFERS_URL_V1 + "/upselling";
    return webClient.post()
            .uri(uri)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(Object.class)
            .doOnSuccess(response -> log.info("Ofertas alternativas obtenidas exitosamente"))
            .doOnError(error ->
                    log.error("Error al obtener ofertas alternativas: {}", error.getMessage()))
            .onErrorResume(WebClientResponseException.class, e -> {
              throw errorHandler.handleAmadeusError(e);
            });
  }

  /**
   * Busca ciudades por palabra clave.
   *
   * @param keyword Palabra clave para buscar.
   * @param token Token de autenticación de Amadeus.
   * @return Resultados de la búsqueda.
   */
  public Mono<Object> searchCities(String keyword, String token) {

    String uri = UriComponentsBuilder.fromPath(amadeusConfig.getBaseUrl()
                    + LOCATIONS_PATH + "/cities")
            .queryParam("keyword", keyword).build().toUriString();

    return webClient.get()
            .uri(uri)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .retrieve()
            .bodyToMono(Object.class)
            .onErrorResume(WebClientResponseException.class, e -> {
              throw errorHandler.handleAmadeusError(e);
            });
  }

  /**
   * Verifica y devuelve una oferta de vuelo con el precio actualizado.
   *
   * @param flightOffer objeto que contiene la oferta de vuelo a verificar
   * @param token token de autenticación
   * @return oferta de vuelo con precio actualizado
   */
  public Mono<Object> verifyFlightOfferPrice(Object flightOffer, String token) {
    log.info("Verificando el precio de la oferta de vuelo");

    String uri = amadeusConfig.getBaseUrl() + FLIGHT_OFFERS_URL_V1 + "/pricing";

    return webClient.post()
            .uri(uri)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(flightOffer)
            .retrieve()
            .bodyToMono(Object.class)
            .doOnSuccess(response -> log.info("Precio verificado para la oferta"))
            .doOnError(error -> log.error("Error al verificar el precio: {}", error.getMessage()))
            .onErrorResume(WebClientResponseException.class, e -> {
              throw errorHandler.handleAmadeusError(e);
            });
  }

  /**
   * Construye la URL para la búsqueda de vuelos.
   *
   * @param flightSearchRequest representa los parámetros de búsqueda de vuelos.
   * @return la URL construida.
   */
  private String getUrl(FlightSearchRequest flightSearchRequest) {
    return UriComponentsBuilder.fromPath(amadeusConfig.getBaseUrl() + FLIGHT_OFFERS_URL_V2)
            .queryParam("originLocationCode", flightSearchRequest.getOrigin())
            .queryParam("destinationLocationCode", flightSearchRequest.getDestination())
            .queryParam("departureDate", flightSearchRequest.getDepartureDate())
            .queryParam("adults", flightSearchRequest.getAdults())
            .queryParamIfPresent("returnDate",
                    Optional.ofNullable(flightSearchRequest.getReturnDate()))
            .queryParamIfPresent("children", Optional.ofNullable(flightSearchRequest.getChildren()))
            .queryParamIfPresent("infants", Optional.ofNullable(flightSearchRequest.getInfants()))
            .queryParamIfPresent("travelClass",
                    Optional.ofNullable(flightSearchRequest.getTravelClass()))
            .queryParamIfPresent("currencyCode",
                    Optional.ofNullable(flightSearchRequest.getCurrency()))
            .queryParamIfPresent("max", Optional.ofNullable(flightSearchRequest.getMaxResults()))
            .queryParamIfPresent("nonStop", Optional.ofNullable(flightSearchRequest.getNonStop()))
            .build()
            .toUriString();
  }
}
