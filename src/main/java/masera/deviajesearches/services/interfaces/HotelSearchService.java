package masera.deviajesearches.services.interfaces;

import masera.deviajesearches.dtos.amadeus.request.HotelOffersRequest;
import masera.deviajesearches.dtos.amadeus.request.HotelRequest;
import org.springframework.stereotype.Service;

/**
 * Interfaz que define los métodos para la búsqueda de hoteles.
 */
@Service
public interface HotelSearchService {

  /**
   * Metodo para buscar hoteles por ciudad.
   *
   * @param request criterios de búsqueda de hoteles.
   * @return una lista de hoteles encontrados.
   */
  Object findHotelsByCity(HotelRequest request);

  /**
   * Busca ofertas disponibles para hoteles específicos.
   *
   * @param request criterios de búsqueda de ofertas.
   * @return resultados de la búsqueda de ofertas.
   */
  Object findHotelOffers(HotelOffersRequest request);

  /**
   * Obtiene detalles de una oferta específica.
   *
   * @param offerId Id de la oferta.
   * @return detalles de la oferta.
   */
  Object getHotelOfferDetails(String offerId);
}
