package masera.deviajesearches.controllers;

import lombok.RequiredArgsConstructor;
import masera.deviajesearches.dtos.amadeus.response.IataResponse;
import masera.deviajesearches.services.impl.IataServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Controlador para manejar solicitudes relacionadas con códigos IATA.
 */
@RestController
@RequestMapping("/api/iata")
@RequiredArgsConstructor
public class IataController {

  private final IataServiceImpl iataService;

  /**
   * Obtiene la información de un aeropuerto por su código IATA.
   *
   * @param iataCode código IATA del aeropuerto.
   * @return ResponseEntity con la información del aeropuerto.
   */
  @GetMapping("/{iataCode}")
  public Mono<ResponseEntity<IataResponse>> getAirportByIata(@PathVariable String iataCode) {
    return iataService.getAirportByIata(iataCode)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
  }
}
