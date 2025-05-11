package masera.deviajesearches.controllers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import masera.deviajesearches.dtos.amadeus.request.FlightSearchRequest;
import masera.deviajesearches.dtos.amadeus.response.CityDto;
import masera.deviajesearches.services.interfaces.FlightSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para la búsqueda de vuelos.
 * Este controlador maneja las solicitudes relacionadas con la búsqueda de vuelos.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/searches/flights")
public class FlightSearchController {

  private final FlightSearchService flightSearchService;

  /**
   * Metodo para buscar vuelos.
   *
   * @param request parámetro de búsqueda de vuelos.
   * @return una respuesta con los resultados de la búsqueda.
   */
  @PostMapping
  public ResponseEntity<Object> searchFlights(@RequestBody FlightSearchRequest request) {
    Object result = flightSearchService.searchFlights(request);
    return ResponseEntity.ok(result);
  }

  /**
   * Endpoint para obtener ofertas alternativas de un vuelo (upselling).
   *
   * @param request objeto que contiene la oferta de vuelo original
   * @return ofertas alternativas del vuelo
   */
  @PostMapping("/upsell")
  public ResponseEntity<Object> getUpsellOffers(@RequestBody Object request) {
    Object result = flightSearchService.searchFlightOfferUpsell(request);
    return ResponseEntity.ok(result);
  }

  /**
   * Endpoint para verificar y actualizar el precio de una oferta de vuelo.
   *
   * @param request objeto que contiene la oferta de vuelo a verificar
   * @return oferta de vuelo con precio actualizado
   */
  @PostMapping("/pricing")
  public ResponseEntity<Object> verifyFlightPricing(@RequestBody Object request) {
    Object result = flightSearchService.verifyFlightOfferPricing(request);
    return ResponseEntity.ok(result);
  }

  /**
   * Endpoint para buscar ciudades y aeropuertos.
   *
   * @param keyword Palabra clave para buscar.
   * @return Resultados de la búsqueda.
   */
  @GetMapping("/cities")
  public ResponseEntity<List<CityDto>> searchCities(@RequestParam String keyword) {
    List<CityDto> result = flightSearchService.searchCities(keyword);
    return ResponseEntity.ok(result);
  }
}
