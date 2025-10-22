package masera.deviajesearches.controllers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import masera.deviajesearches.dtos.amadeus.response.CityDto;
import masera.deviajesearches.dtos.amadeus.response.CountryDto;
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
@RequestMapping("/api-searches/hotels/content")
@RequiredArgsConstructor
public class HotelContentController {

  private final HotelContentService hotelContentService;

  /**
   * Carga hoteles desde la API de Hotelbeds.
   * Si se proporciona lastUpdateTime, obtiene solo las actualizaciones.
   *
   * @param from índice inicial (por defecto 1)
   * @param to índice final (por defecto 1000)
   * @param language idioma (por defecto ENG)
   * @param lastUpdateTime (Opcional) fecha de última actualización en formato YYYY-MM-DD
   * @return lista de hoteles cargados
   */
  @PostMapping("/hotels/load")
  public ResponseEntity<String> loadHotels(
          @RequestParam(defaultValue = "1") int from,
          @RequestParam(defaultValue = "1000") int to,
          @RequestParam(defaultValue = "CAS") String language,
          @RequestParam(required = false) String lastUpdateTime) {

    Integer size = hotelContentService.loadHotels(from, to, language, lastUpdateTime);
    String message = lastUpdateTime != null
            ? "Hoteles actualizados: " + size + " desde " + lastUpdateTime
            : "Hoteles cargados: " + size + " desde " + from + " hasta " + to;

    return ResponseEntity.ok(message + " en idioma " + language);
  }

  /**
   * Carga países desde la API de Hotelbeds.
   *
   * @param language idioma (por defecto ENG)
   * @return respuesta con el resultado de la carga
   */
  @PostMapping("/countries/load")
  public ResponseEntity<String> loadCountries(
          @RequestParam(defaultValue = "1") int from,
          @RequestParam(defaultValue = "1000") int to,
          @RequestParam(defaultValue = "CAS") String language,
          @RequestParam(required = false) String lastUpdateTime) {

    Integer size = hotelContentService.loadCountries(
            from, to, language, lastUpdateTime);

    String message = lastUpdateTime != null
            ? "Países actualizados: " + size + " desde " + lastUpdateTime
            : "Países cargados: " + size + " desde " + from + " hasta " + to;
    return ResponseEntity.ok(message);
  }

  /**
   * Carga destinos desde la API de Hotelbeds.
   *
   * @param from índice inicial (por defecto 1)
   * @param to índice final (por defecto 1000)
   * @param language idioma (por defecto ENG)
   * @return respuesta con el resultado de la carga
   */
  @PostMapping("/destinations/load")
  public ResponseEntity<String> loadDestinations(
          @RequestParam(defaultValue = "1") int from,
          @RequestParam(defaultValue = "1000") int to,
          @RequestParam(defaultValue = "CAS") String language,
          @RequestParam(required = false) String lastUpdateTime) {

    Integer size = hotelContentService.loadDestinations(
            from, to, language, lastUpdateTime);

    String message = lastUpdateTime != null
            ? "Destinos actualizados: " + size + " desde " + lastUpdateTime
            : "Destinos cargados: " + size + " desde " + from + " hasta " + to;

    return ResponseEntity.ok(message);
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
   * Endpoint para buscar destinos de hoteles.
   *
   * @param keyword Palabra clave para buscar.
   * @return Resultados de la búsqueda.
   */
  @GetMapping("/destinations")
  public ResponseEntity<List<CityDto>> searchDestinations(@RequestParam String keyword) {
    List<CityDto> result = hotelContentService.searchDestinations(keyword);
    return ResponseEntity.ok(result);
  }
}