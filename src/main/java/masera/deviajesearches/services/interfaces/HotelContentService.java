package masera.deviajesearches.services.interfaces;

import java.util.List;
import java.util.Map;
import masera.deviajesearches.dtos.amadeus.response.CityDto;
import masera.deviajesearches.dtos.amadeus.response.CountryDto;
import org.springframework.stereotype.Service;

/**
 * Interfaz que define los métodos para la carga y actualización de hoteles.
 */
@Service
public interface HotelContentService {

  /**
   * Carga hoteles desde la API de Hotelbeds.
   *
   * @param from índice inicial
   * @param to índice final
   * @param language idioma
   * @return lista de hoteles cargados
   */
  List<Object> loadHotels(int from, int to, String language);

  /**
   * Actualiza hoteles desde la API de Hotelbeds desde una fecha específica.
   *
   * @param from índice inicial
   * @param to índice final
   * @param language idioma
   * @param lastUpdateTime fecha de última actualización
   * @return lista de hoteles actualizados
   */
  List<Object> updateHotels(int from, int to, String language, String lastUpdateTime);

  /**
   * Obtiene todos los países de la base de datos.
   *
   * @return lista de países
   */
  List<CountryDto> getAllCountries();


  /**
   * Busca países por un término de búsqueda.
   *
   * @param keyword término de búsqueda
   * @return lista de países que coinciden con el término de búsqueda
   */
  List<CityDto> searchDestinations(String keyword);

  /**
   * Carga países desde la API de Hotelbeds y los guarda en la base de datos.
   *
   * @param language idioma
   * @return mapa con la cantidad de países cargados y otros datos relevantes
   */
  Map<String, Object> loadCountries(String language);

  /**
   * Carga destinos desde la API de Hotelbeds y los guarda en la base de datos.
   *
   * @param countryCode código de país opcional (puede ser null)
   * @param language idioma
   * @return mapa con la cantidad de destinos cargados y otros datos relevantes
   */
  Map<String, Object> loadDestinations(String countryCode, String language);
}

