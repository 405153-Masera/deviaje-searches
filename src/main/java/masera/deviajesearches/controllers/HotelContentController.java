package masera.deviajesearches.controllers;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import masera.deviajesearches.dtos.amadeus.response.CountryDto;
import masera.deviajesearches.entities.Destination;
import masera.deviajesearches.services.interfaces.HotelContentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para la gestión de contenido de hoteles.
 */
@RestController
@RequestMapping("/api/searches/hotels/content")
@RequiredArgsConstructor
public class HotelContentController {

  private final HotelContentService hotelContentService;

  /**
   * Carga hoteles desde la API de Hotelbeds.
   *
   * @param from índice inicial (por defecto 1)
   * @param to índice final (por defecto 1000)
   * @param language idioma (por defecto ENG)
   * @return lista de hoteles cargados
   */
  @GetMapping("/load")
  public ResponseEntity<String> loadHotels(
          @RequestParam(defaultValue = "1") int from,
          @RequestParam(defaultValue = "1000") int to,
          @RequestParam(defaultValue = "CAS") String language) {

    List<Object> hotels = hotelContentService.loadHotels(from, to, language);
    return ResponseEntity.ok("Hoteles cargados: " + hotels.size()
            + " desde " + from + " hasta " + to + " en idioma " + language);
  }

  /**
   * Actualiza hoteles desde la API de Hotelbeds.
   *
   * @param from índice inicial (por defecto 1)
   * @param to índice final (por defecto 1000)
   * @param language idioma (por defecto ENG)
   * @param lastUpdateTime fecha de última actualización
   * @return lista de hoteles actualizados
   */
  @GetMapping("/update")
  public ResponseEntity<List<Object>> updateHotels(
          @RequestParam(defaultValue = "1") int from,
          @RequestParam(defaultValue = "1000") int to,
          @RequestParam(defaultValue = "CAS") String language,
          @RequestParam String lastUpdateTime) {

    List<Object> hotels = hotelContentService.updateHotels(from, to, language, lastUpdateTime);
    return ResponseEntity.ok(hotels);
  }

  /**
   * Carga países desde la API de Hotelbeds.
   *
   * @param language idioma (por defecto ENG)
   * @return respuesta con el resultado de la carga
   */
  @PostMapping("/countries/load")
  public ResponseEntity<Map<String, Object>> loadCountries(
          @RequestParam(defaultValue = "CAS") String language) {

    Map<String, Object> result = hotelContentService.loadCountries(language);
    return ResponseEntity.ok(result);
  }

  /**
   * Carga destinos desde la API de Hotelbeds.
   *
   * @param countryCode código de país opcional
   * @param language idioma (por defecto ENG)
   * @return respuesta con el resultado de la carga
   */
  @PostMapping("/destinations/load")
  public ResponseEntity<Map<String, Object>> loadDestinations(
          @RequestParam(required = false) String countryCode,
          @RequestParam(defaultValue = "ENG") String language) {

    Map<String, Object> result = hotelContentService.loadDestinations(countryCode, language);
    return ResponseEntity.ok(result);
  }

  /**
   * Obtiene todos los países guardados en la base de datos.
   *
   * @return lista de países
   */
  @GetMapping("/countries")
  public ResponseEntity<List<CountryDto>> getAllCountries() {
    List<CountryDto> countries = hotelContentService.getAllCountries();
    return ResponseEntity.ok(countries);
  }

  /**
   * Obtiene todos los destinos guardados en la base de datos.
   *
   * @return lista de destinos
   */
  @GetMapping("/destinations")
  public ResponseEntity<List<Destination>> getAllDestinations() {
    List<Destination> destinations = hotelContentService.getAllDestinations();
    return ResponseEntity.ok(destinations);
  }
}