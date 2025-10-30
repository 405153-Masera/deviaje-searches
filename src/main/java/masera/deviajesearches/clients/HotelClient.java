package masera.deviajesearches.clients;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masera.deviajesearches.configs.HotelbedsConfig;
import masera.deviajesearches.dtos.amadeus.request.HotelSearchRequest;
import masera.deviajesearches.dtos.amadeus.response.HotelSearchResponse;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.CountriesResponse;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.HotelContentResponse;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.accommodations.AccommodationResponse;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.boards.BoardsResponse;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.categories.CategoriesResponse;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.chains.ChainsResponse;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.destinations.DestinationsResponse;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.facilities.FacilitiesResponse;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.facilities.FacilityGroupsResponse;
import masera.deviajesearches.utils.ErrorHandler;
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

  private final ErrorHandler errorHandler;

  private static final String AVAILABILITY_ENDPOINT = "/hotel-api/1.0/hotels";

  private static final String CONTENT_ENDPOINT = "/hotel-content-api/1.0/hotels";

  private static final String LOCATIONS_ENDPOINT = "/hotel-content-api/1.0/locations";

  private  static final String BASE_ENDPOINT = "/hotel-content-api/1.0";

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
            .onErrorResume(WebClientResponseException.class, e -> {
              log.error("Error al buscar ofertas de hoteles - Status: {}, Body: {}",
                      e.getStatusCode(), e.getResponseBodyAsString());
              throw errorHandler.handleHotelBedsError(e);
            });
  }

  /**
   * Obtiene hoteles desde la API de contenido de Hotelbeds.
   *
   * @param from índice inicial
   * @param to índice final
   * @param language idioma
   * @param lastUpdateTime (Opcional) fecha de última actualización
   * @return Mono con la respuesta de contenido de hoteles
   */
  public Mono<HotelContentResponse> getHotelContent(
          int from, int to, String language, String lastUpdateTime) {

    log.info("Obteniendo hoteles desde {} hasta {} en idioma {}", from, to, language);

    String uri = buildUriWithParams(from, to, language, lastUpdateTime, CONTENT_ENDPOINT);

    return webClient.get()
            .uri(hotelbedsConfig.getBaseUrl() + uri)
            .headers(this::addHotelbedsHeaders)
            .retrieve()
            .bodyToMono(HotelContentResponse.class)
            .doOnSuccess(response -> log.info("Contenido de hoteles obtenido exitosamente"))
            .doOnError(error -> log.error("Error al obtener contenido de hoteles: {}",
                    error.getMessage()))
            .onErrorResume(WebClientResponseException.class, e -> {
              log.error("Error al buscar el contenido de hoteles - Status: {}, Body: {}",
                      e.getStatusCode(), e.getResponseBodyAsString());
              throw errorHandler.handleHotelBedsError(e);
            });
  }

  /**
   * Obtiene países desde la API de contenido de Hotelbeds.
   *
   * @param from índice inicial
   * @param to índice final
   * @param language idioma
   * @param lastUpdateTime (Opcional) fecha de última actualización
   * @return Mono con la respuesta de países
   */
  public Mono<CountriesResponse> getCountries(
          int from, int to, String language, String lastUpdateTime) {
    log.info("Obteniendo países en idioma {}", language);

    String uri = buildUriWithParams(
            from, to, language, lastUpdateTime, LOCATIONS_ENDPOINT + "/countries");

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
   * @param from índice inicial
   * @param to índice final
   * @param language idioma
   * @param lastUpdateTime (Opcional) fecha de última actualización
   * @return Mono con la respuesta de destinos
   */
  public Mono<DestinationsResponse> getDestinations(
          int from, int to, String language, String lastUpdateTime) {

    String uri = buildUriWithParams(
            from, to, language, lastUpdateTime, LOCATIONS_ENDPOINT + "/destinations");

    return webClient.get()
            .uri(hotelbedsConfig.getBaseUrl() + uri)
            .headers(this::addHotelbedsHeaders)
            .retrieve()
            .bodyToMono(DestinationsResponse.class)
            .doOnSuccess(response -> log.info("Destinos obtenidos exitosamente"))
            .doOnError(error -> log.error("Error al obtener destinos: {}", error.getMessage()));
  }

  /**
   * Obtiene contenido de alojamientos desde la API de Hotelbeds.
   *
   * @param from índice inicial
   * @param to índice final
   * @param language idioma
   * @param lastUpdateTime (Opcional) fecha de última actualización
   * @return Mono con la respuesta de contenido de alojamientos
   */
  public Mono<AccommodationResponse> getAccommodations(
          int from, int to, String language, String lastUpdateTime) {

    String uri = buildUriWithParams(
            from, to, language, lastUpdateTime, BASE_ENDPOINT + "/types/accommodations");

    return webClient.get()
            .uri(hotelbedsConfig.getBaseUrl() + uri)
            .headers(this::addHotelbedsHeaders)
            .retrieve()
            .bodyToMono(AccommodationResponse.class)
            .doOnSuccess(response -> log.info("Tipos de alojamientos obtenidos exitosamente"))
            .doOnError(error -> log.error(
                    "Error al obtener tipos de alojamientos: {}", error.getMessage()))
            .onErrorResume(WebClientResponseException.class, e -> {
              log.error("Error al buscar tipos de alojamientos - Status: {}, Body: {}",
                      e.getStatusCode(), e.getResponseBodyAsString());
              throw errorHandler.handleHotelBedsError(e);
            });

  }

  /**
   * Obtiene regímenes de alimentos desde la API de Hotelbeds.
   *
   * @param from índice inicial
   * @param to índice final
   * @param language idioma
   * @param lastUpdateTime (Opcional) fecha de última actualización
   * @return Mono con la respuesta de regímenes de alimentos
   */
  public  Mono<BoardsResponse> getBoards(
            int from, int to, String language, String lastUpdateTime) {

    String uri = buildUriWithParams(
            from, to, language, lastUpdateTime, BASE_ENDPOINT + "/types/boards");

    return webClient.get()
            .uri(hotelbedsConfig.getBaseUrl() + uri)
            .headers(this::addHotelbedsHeaders)
            .retrieve()
            .bodyToMono(BoardsResponse.class)
            .doOnSuccess(response ->
                    log.info("Regímenes de alimentos obtenidos exitosamente"))
            .doOnError(error -> log.error(
                    "Error al obtener regímenes de alimentos: {}", error.getMessage()))
            .onErrorResume(WebClientResponseException.class, e -> {
              log.error("Error al buscar regímenes de alojamientos - Status: {}, Body: {}",
                      e.getStatusCode(), e.getResponseBodyAsString());
              throw errorHandler.handleHotelBedsError(e);
            });
  }

  /**
   * Obtiene categorías desde la API de Hotelbeds.
   *
   * @param from índice inicial
   * @param to índice final
   * @param language idioma
   * @param lastUpdateTime (Opcional) fecha de última actualización
   * @return Mono con la respuesta de categorías
   */
  public Mono<CategoriesResponse> getCategories(
          int from, int to, String language, String lastUpdateTime) {

    String uri = buildUriWithParams(
            from, to, language, lastUpdateTime, BASE_ENDPOINT + "/types/categories");

    return webClient.get()
            .uri(hotelbedsConfig.getBaseUrl() + uri)
            .headers(this::addHotelbedsHeaders)
            .retrieve()
            .bodyToMono(CategoriesResponse.class)
            .doOnSuccess(response ->
                    log.info("Categorías de hoteles obtenidos exitosamente"))
            .doOnError(error -> log.error(
                    "Error al obtener Categorías de hoteles: {}", error.getMessage()))
            .onErrorResume(WebClientResponseException.class, e -> {
              log.error("Error al buscarCategorías de hoteles - Status: {}, Body: {}",
                      e.getStatusCode(), e.getResponseBodyAsString());
              throw errorHandler.handleHotelBedsError(e);
            });

  }

  /**
   * Obtiene instalaciones desde la API de Hotelbeds.
   *
   * @param from índice inicial
   * @param to índice final
   * @param language idioma
   * @param lastUpdateTime (Opcional) fecha de última actualización
   * @return Mono con la respuesta de instalaciones
   */
  public Mono<FacilitiesResponse> getFacilities(
          int from, int to, String language, String lastUpdateTime) {

    String uri = buildUriWithParams(
            from, to, language, lastUpdateTime, BASE_ENDPOINT + "/types/facilities");

    return webClient.get()
            .uri(hotelbedsConfig.getBaseUrl() + uri)
            .headers(this::addHotelbedsHeaders)
            .retrieve()
            .bodyToMono(FacilitiesResponse.class)
            .doOnSuccess(response ->
                    log.info("Instalaciones de hoteles obtenidos exitosamente"))
            .doOnError(error -> log.error(
                    "Error al obtener instalaciones de hoteles: {}", error.getMessage()))
            .onErrorResume(WebClientResponseException.class, e -> {
              log.error("Error al buscar instalaciones de hoteles - Status: {}, Body: {}",
                      e.getStatusCode(), e.getResponseBodyAsString());
              throw errorHandler.handleHotelBedsError(e);
            });

  }

  /**
   * Obtiene grupos de instalaciones desde la API de Hotelbeds.
   *
   * @param from índice inicial
   * @param to índice final
   * @param language idioma
   * @param lastUpdateTime (Opcional) fecha de última actualización
   * @return Mono con la respuesta de grupos de instalaciones
   */
  public Mono<FacilityGroupsResponse> getFacilityGroups(
          int from, int to, String language, String lastUpdateTime) {

    String uri = buildUriWithParams(
            from, to, language, lastUpdateTime, BASE_ENDPOINT + "/types/facilitygroups");

    return webClient.get()
            .uri(hotelbedsConfig.getBaseUrl() + uri)
            .headers(this::addHotelbedsHeaders)
            .retrieve()
            .bodyToMono(FacilityGroupsResponse.class)
            .doOnSuccess(response ->
                    log.info("Grupos de instalaciones de hoteles obtenidos exitosamente"))
            .doOnError(error -> log.error(
                    "Error al obtener grupos de instalaciones de hoteles: {}", error.getMessage()))
            .onErrorResume(WebClientResponseException.class, e -> {
              log.error("Error al buscar grupos de instalaciones de hoteles - Status: {}, Body: {}",
                      e.getStatusCode(), e.getResponseBodyAsString());
              throw errorHandler.handleHotelBedsError(e);
            });

  }

  /**
   * Obtiene cadenas hoteleras desde la API de Hotelbeds.
   *
   * @param from índice inicial
   * @param to índice final
   * @param language idioma
   * @param lastUpdateTime (Opcional) fecha de última actualización
   * @return Mono con la respuesta de cadenas hoteleras
   */
  public Mono<ChainsResponse> getChains(
            int from, int to, String language, String lastUpdateTime) {

    String uri = buildUriWithParams(
            from, to, language, lastUpdateTime, BASE_ENDPOINT + "/types/chains");
    return webClient.get()
            .uri(hotelbedsConfig.getBaseUrl() + uri)
            .headers(this::addHotelbedsHeaders)
            .retrieve()
            .bodyToMono(ChainsResponse.class)
            .doOnSuccess(response -> log.info("Cadenas hoteleras obtenidas exitosamente"))
            .doOnError(error -> log.error(
                    "Error al obtener cadenas hoteleras: {}", error.getMessage()))
            .onErrorResume(WebClientResponseException.class, e -> {
              log.error("Error al buscar cadenas hoteleras - Status: {}, Body: {}",
                      e.getStatusCode(), e.getResponseBodyAsString());
              throw errorHandler.handleHotelBedsError(e);
            });
  }

  private void addHotelbedsHeaders(HttpHeaders headers) {
    long timestamp = System.currentTimeMillis() / 1000;
    String signature = DigestUtils.sha256Hex(hotelbedsConfig.getApiKey()
            + hotelbedsConfig.getApiSecret() + timestamp);

    headers.set("Api-Key", hotelbedsConfig.getApiKey());
    headers.set("X-Signature", signature);
    headers.set("Accept", "application/json");
  }

  private String buildUriWithParams(
          int from, int to, String language, String lastUpdateTime, String urlBase) {

    UriComponentsBuilder builder = UriComponentsBuilder.fromPath(urlBase)
            .queryParam("fields", "all")
            .queryParam("language", language)
            .queryParam("from", from)
            .queryParam("to", to)
            .queryParam("useSecondaryLanguage", true);

    if (lastUpdateTime != null && !lastUpdateTime.isEmpty()) {
      builder.queryParam("lastUpdateTime", lastUpdateTime);
    }

    return builder.build().toUriString();
  }
}
