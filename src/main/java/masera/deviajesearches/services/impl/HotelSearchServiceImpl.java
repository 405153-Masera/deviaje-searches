package masera.deviajesearches.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masera.deviajesearches.clients.HotelClient;
import masera.deviajesearches.services.interfaces.HotelSearchService;
import org.springframework.stereotype.Service;

/**
 * Implementación del servicio de búsqueda de hoteles.
 * Esta clase se encarga de la lógica de negocio relacionada con la búsqueda de hoteles.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class HotelSearchServiceImpl implements HotelSearchService {

  private final HotelClient hotelClient;

}
