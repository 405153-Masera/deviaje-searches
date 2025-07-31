package masera.deviajesearches.clients;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masera.deviajesearches.configs.IataConfig;
import masera.deviajesearches.dtos.amadeus.response.IataResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Cliente para consumir la API de Airports de API Ninjas.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class IataClient {

  private final WebClient webClient;
  private final IataConfig iataConfig;

  private static final String AIRPORTS_ENDPOINT = "/v1/airports";

  /**
   * Obtiene información de un aeropuerto por su código IATA.
   *
   * @param iataCode código IATA del aeropuerto.
   * @return Mono con la respuesta del aeropuerto.
   */
  public Mono<IataResponse> getAirportByIata(String iataCode) {
    log.info("Buscando aeropuerto con código IATA: {}", iataCode);

    return webClient
            .get()
            .uri(uriBuilder -> uriBuilder
                    .path(iataConfig.getBaseUrl() + AIRPORTS_ENDPOINT)
                    .queryParam("iata", iataCode)
                    .build())
            .header("X-Api-Key", iataConfig.getApiKey())
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(IataResponse.class)
            .doOnSuccess(response -> log.info("Búsqueda de aeropuerto completada exitosamente"))
            .doOnError(error -> log.error("Error al obtener aeropuerto: {}", error.getMessage()));
  }
}