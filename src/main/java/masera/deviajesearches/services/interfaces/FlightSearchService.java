package masera.deviajesearches.services.interfaces;

import java.util.List;
import masera.deviajesearches.dtos.amadeus.request.FlightSearchRequest;
import masera.deviajesearches.dtos.amadeus.response.CityDto;
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

  /**
   * Obtiene ofertas alternativas de un vuelo (upselling).
   *
   * @param flightOffer objeto que contiene la oferta de vuelo original
   * @return ofertas alternativas del vuelo
   */
  Object searchFlightOfferUpsell(Object flightOffer);

  /**
   * Busca ciudades por palabra clave.
   *
   * @param keyword Palabra clave para buscar.
   * @return Resultados de la búsqueda.
   */
  List<CityDto> searchCities(String keyword);
}
