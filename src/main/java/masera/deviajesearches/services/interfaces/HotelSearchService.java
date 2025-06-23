package masera.deviajesearches.services.interfaces;

import masera.deviajesearches.dtos.amadeus.request.HotelSearchRequest;
import masera.deviajesearches.dtos.amadeus.response.HotelResponseDto;
import masera.deviajesearches.dtos.amadeus.response.HotelSearchResponse;
import org.springframework.stereotype.Service;

/**
 * Interfaz que define los métodos para la búsqueda de hoteles.
 */
@Service
public interface HotelSearchService {

  /**
   * Busca hoteles según los criterios especificados.
   *
   * @param request solicitud de búsqueda de hoteles
   * @return respuesta con los resultados de la búsqueda
   */
  HotelSearchResponse searchHotels(HotelSearchRequest request);

  /**
   * Obtiene detalles de un hotel específico.
   *
   * @param hotelCode código del hotel
   * @return detalles del hotel
   */
  HotelResponseDto getHotelDetails(String hotelCode);
}
