package masera.deviajesearches.controllers;

import lombok.RequiredArgsConstructor;
import masera.deviajesearches.dtos.amadeus.request.FlightSearchRequest;
import masera.deviajesearches.services.interfaces.FlightSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
