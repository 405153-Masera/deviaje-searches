package masera.deviajesearches.clients;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masera.deviajesearches.dtos.amadeus.request.FlightSearchRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
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

  private static  final String FLIGHT_OFFERS_URL = "/v2/shopping/flight-offers";

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
            .doOnError(error -> log.error("Error al buscar vuelos: {}", error.getMessage()));
  }

  /**
   * Construye la URL para la búsqueda de vuelos.
   *
   * @param flightSearchRequest representa los parámetros de búsqueda de vuelos.
   * @return la URL construida.
   */
  private String getUrl(FlightSearchRequest flightSearchRequest) {
    return UriComponentsBuilder.fromPath(FLIGHT_OFFERS_URL)
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
