package masera.deviajesearches.services.interfaces;

import masera.deviajesearches.dtos.amadeus.request.FlightSearchRequest;
import org.springframework.stereotype.Service;

/**
 * Servicio para la búsqueda de vuelos.
 */
@Service
public interface FlightSearchService {

  /**
   * Realiza una búsqueda de vuelos utilizando la API de Amadeus.
   *
   * @param flightSearchRequest el objeto que contiene los parámetros de búsqueda.
   * @return un objeto que representa la respuesta de la búsqueda de vuelos.
   */
  Object searchFlights(FlightSearchRequest flightSearchRequest);
}
