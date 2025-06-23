package masera.deviajesearches.clients;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masera.deviajesearches.configs.HotelbedsConfig;
import masera.deviajesearches.dtos.amadeus.request.HotelSearchRequest;
import masera.deviajesearches.dtos.amadeus.response.HotelSearchResponse;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.CountriesResponse;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.HotelContentResponse;
import masera.deviajesearches.utils.AmadeusErrorHandler;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
  private static final String LOCATIONS_ENDPOINT = "/hotel-content-api/1.0/locations";

  /**
   * Obtiene una lista de hoteles según los criterios de búsqueda.
   *
   * @param request parámetros de búsqueda de hoteles.
   * @return una lista de hoteles.
  +*/
  public Mono<HotelSearchResponse> searchHotels(HotelSearchRequest request) {
    log.info("Buscando hoteles en Hotelbeds con destino: {}", request.getDestination().getCode());

    return webClient
            .post()
            .uri(hotelbedsConfig.getBaseUrl() + AVAILABILITY_ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON)
            .headers(this::addHotelbedsHeaders)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(HotelSearchResponse.class)
            .doOnSuccess(response -> log.info("Búsqueda de hoteles completada exitosamente"))
            .doOnError(error -> log.error("Error al buscar hoteles: {}", error.getMessage()))
            .onErrorResume(WebClientResponseException.class, e -> {
              log.error("Error de respuesta de Hotelbeds: {}", e.getResponseBodyAsString());
              throw errorHandler.handleAmadeusError(e);
            });
  }

  /**
   * Obtiene hoteles desde la API de contenido de Hotelbeds.
   *
   * @param from índice inicial
   * @param to índice final
   * @param language idioma
   * @return Mono con la respuesta de contenido de hoteles
   */
  public Mono<HotelContentResponse> getHotelContent(int from, int to, String language) {
    log.info("Obteniendo contenido de hoteles desde {} hasta {} en idioma {}", from, to, language);

    String uri = UriComponentsBuilder.fromPath(CONTENT_ENDPOINT)
            .queryParam("fields", "all")
            .queryParam("language", language)
            .queryParam("from", from)
            .queryParam("to", to)
            .build().toUriString();

    return webClient.get()
            .uri(hotelbedsConfig.getBaseUrl() + uri)
            .headers(this::addHotelbedsHeaders)
            .retrieve()
            .bodyToMono(HotelContentResponse.class)
            .doOnSuccess(response -> log.info("Contenido de hoteles obtenido exitosamente"))
            .doOnError(error -> log.error("Error al obtener contenido de hoteles: {}",
                    error.getMessage()));
  }

  /**
   * Obtiene actualizaciones de hoteles desde la API de contenido de Hotelbeds.
   *
   * @param from índice inicial
   * @param to índice final
   * @param language idioma
   * @param lastUpdateTime fecha de última actualización
   * @return Mono con la respuesta de contenido de hoteles
   */
  public Mono<HotelContentResponse> getHotelContentUpdates(int from, int to,
                                                           String language, String lastUpdateTime) {

    log.info("Obteniendo actualizaciones de hoteles desde {} con lastUpdateTime {}",
            from, lastUpdateTime);

    String uri = UriComponentsBuilder.fromPath(CONTENT_ENDPOINT)
            .queryParam("fields", "all")
            .queryParam("language", language)
            .queryParam("from", from)
            .queryParam("to", to)
            .queryParam("lastUpdateTime", lastUpdateTime)
            .build().toUriString();

    return webClient.get()
            .uri(hotelbedsConfig.getBaseUrl() + uri)
            .headers(this::addHotelbedsHeaders)
            .retrieve()
            .bodyToMono(HotelContentResponse.class)
            .doOnSuccess(response -> log.info("Actualizaciones de hoteles obtenidas exitosamente"))
            .doOnError(error -> log.error("Error al obtener actualizaciones de hoteles: {}",
                    error.getMessage()));
  }


  /**
   * Obtiene países desde la API de contenido de Hotelbeds.
   *
   * @param language idioma
   * @return Mono con la respuesta de países
   */
  public Mono<CountriesResponse> getCountries(String language) {
    log.info("Obteniendo países en idioma {}", language);

    String uri = UriComponentsBuilder.fromPath(LOCATIONS_ENDPOINT + "/countries")
            .queryParam("language", language)
            .queryParam("fields", "all")
            .queryParam("from", 1)
            .queryParam("to", 1000)
            .build().toUriString();

    return webClient.get()
            .uri(hotelbedsConfig.getBaseUrl() + uri)
            .headers(this::addHotelbedsHeaders)
            .retrieve()
            .bodyToMono(CountriesResponse.class)
            .doOnSuccess(response -> log.info("Países obtenidos exitosamente"))
            .doOnError(error -> log.error("Error al obtener países: {}", error.getMessage()));
  }


  /**
   * Obtiene destinos desde la API de contenido de Hotelbeds.
   *
   * @param countryCode código de país opcional
   * @param language idioma
   * @return Mono con la respuesta de destinos
   */
  public Mono<Object> getDestinations(String countryCode, String language) {
    log.info("Obteniendo destinos para país {} en idioma {}", countryCode, language);

    UriComponentsBuilder uriBuilder = UriComponentsBuilder
            .fromPath(LOCATIONS_ENDPOINT + "/destinations")
            .queryParam("language", language)
            .queryParam("fields", "all")
            .queryParam("from", 7001)
            .queryParam("to", 8000);

    if (countryCode != null && !countryCode.isEmpty()) {
      uriBuilder.queryParam("countryCode", countryCode);
    }

    String uri = uriBuilder.build().toUriString();

    return webClient.get()
            .uri(hotelbedsConfig.getBaseUrl() + uri)
            .headers(this::addHotelbedsHeaders)
            .retrieve()
            .bodyToMono(Object.class)
            .doOnSuccess(response -> log.info("Destinos obtenidos exitosamente"))
            .doOnError(error -> log.error("Error al obtener destinos: {}", error.getMessage()));
  }

  private void addHotelbedsHeaders(HttpHeaders headers) {
    long timestamp = System.currentTimeMillis() / 1000;
    String signature = DigestUtils.sha256Hex(hotelbedsConfig.getApiKey()
            + hotelbedsConfig.getApiSecret() + timestamp);

    headers.set("Api-Key", hotelbedsConfig.getApiKey());
    headers.set("X-Signature", signature);
    headers.set("Accept", "application/json");
  }
}
