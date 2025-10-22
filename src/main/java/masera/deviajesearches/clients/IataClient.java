package masera.deviajesearches.clients;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masera.deviajesearches.configs.IataConfig;
import masera.deviajesearches.dtos.amadeus.response.IataResponse;
import org.springframework.http.HttpStatusCode;
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

    String fullUrl = iataConfig.getBaseUrl() + AIRPORTS_ENDPOINT + "?iata=" + iataCode;
    log.debug("URL completa para la consulta: {}", fullUrl);

    return webClient
            .get()
            .uri(fullUrl)
            .header("X-Api-Key", iataConfig.getApiKey())
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError,
                    response -> response.bodyToMono(String.class)
                            .map(body -> new RuntimeException("Error del cliente: " + body)))
            .onStatus(HttpStatusCode::is5xxServerError,
                    response -> response.bodyToMono(String.class)
                            .map(body -> new RuntimeException("Error del servidor: " + body)))
            .bodyToMono(IataResponse[].class)
            .map(array -> array.length > 0 ? array[0] : null)
            .doOnSuccess(response -> log.info(
                    "Búsqueda de aeropuerto completada exitosamente para IATA: {}", iataCode))
            .doOnError(error -> log.error(
                    "Error al obtener aeropuerto con IATA {}: {}", iataCode, error.getMessage()));
  }
}