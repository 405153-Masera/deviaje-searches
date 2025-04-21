package masera.deviajesearches.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masera.deviajesearches.clients.FlightClient;
import masera.deviajesearches.dtos.amadeus.request.FlightSearchRequest;
import masera.deviajesearches.services.interfaces.AmadeusTokenService;
import masera.deviajesearches.services.interfaces.FlightSearchService;
import org.springframework.stereotype.Service;

/**
 * Clase de implementación del servicio de búsqueda de vuelos.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FlightSearchServiceImp implements FlightSearchService {

  private final FlightClient flightClient;

  private final AmadeusTokenService amadeusTokenService;

  /**
   * Metodo para buscar vuelos.
   *
   * @param flightSearchRequest el objeto que contiene los parámetros de búsqueda.
   * @return una respuesta de búsqueda de vuelos.
   */
  @Override
  public Object searchFlights(FlightSearchRequest flightSearchRequest) {

    log.info("Iniciando búsqueda de vuelos con parámetros: {}", flightSearchRequest);
    try {
      String token = amadeusTokenService.getToken();

      Object flights = flightClient.searchFlightOffers(flightSearchRequest, token).block();

      log.info("Búsqueda de vuelos completada con éxito.");
      return flights;
    } catch (Exception e) {
      log.error("Error de búsqueda de vuelos: {}", e.getMessage());
      throw new RuntimeException("Error al buscar vuelos: " + e.getMessage(), e);
    }
  }
}
