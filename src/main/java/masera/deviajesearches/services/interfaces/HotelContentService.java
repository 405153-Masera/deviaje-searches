package masera.deviajesearches.services.interfaces;

import java.util.List;
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
   * @param lastUpdateTime (Opcional) fecha de última actualización
   * @return cantidad de hoteles cargados
   */
  Integer loadHotels(int from, int to, String language, String lastUpdateTime);

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
   * @param from índice inicial
   * @param to índice final
   * @param lastUpdateTime (Opcional) fecha de última actualización
   * @return cantidad de países cargados
   */
  Integer loadCountries(int from, int to, String language, String lastUpdateTime);

  /**
   * Carga destinos desde la API de Hotelbeds y los guarda en la base de datos.
   *
   * @param from índice inicial
   * @param to índice final
   * @param lastUpdateTime (Opcional) fecha de última actualización
   * @param language idioma
   * @return cantidad de destinos cargados
   */
  Integer loadDestinations(int from, int to, String language, String lastUpdateTime);

  /**
   * Carga tipos de alojamiento desde la API de Hotelbeds y los guarda en la base de datos.
   *
   * @param from índice inicial
   * @param to índice final
   * @param language idioma
   * @param lastUpdateTime (Opcional) fecha de última actualización
   * @return cantidad de tipos de alojamiento cargados
   */
  Integer loadAccommodations(int from, int to, String language, String lastUpdateTime);

  /**
   * Carga regímenes de alimentos desde la API de Hotelbeds y los guarda en la base de datos.
   *
   * @param from índice inicial
   * @param to índice final
   * @param language idioma
   * @param lastUpdateTime (Opcional) fecha de última actualización
   * @return cantidad de regímenes de alimentos cargados
   */
  Integer loadBoards(int from, int to, String language, String lastUpdateTime);

  /**
   * Carga categorías de hoteles desde la API de Hotelbeds y las guarda en la base de datos.
   *
   * @param from índice inicial
   * @param to índice final
   * @param language idioma
   * @param lastUpdateTime (Opcional) fecha de última actualización
   * @return cantidad de categorías cargadas
   */
  Integer loadCategories(int from, int to, String language, String lastUpdateTime);

  /**
   * Carga instalaciones desde la API de Hotelbeds y las guarda en la base de datos.
   *
   * @param from índice inicial
   * @param to índice final
   * @param language idioma
   * @param lastUpdateTime (Opcional) fecha de última actualización
   * @return cantidad de instalaciones cargadas
   */
  Integer loadFacilities(int from, int to, String language, String lastUpdateTime);

  /**
   * Carga grupos de instalaciones desde la API de Hotelbeds y las guarda en la base de datos.
   *
   * @param from índice inicial
   * @param to índice final
   * @param language idioma
   * @param lastUpdateTime (Opcional) fecha de última actualización
   * @return cantidad de grupos de instalaciones cargadas
   */
  Integer loadFacilityGroups(int from, int to, String language, String lastUpdateTime);

  /**
   * Carga cadenas hoteleras desde la API de Hotelbeds y las guarda en la base de datos.
   *
   * @param from índice inicial
   * @param to índice final
   * @param language idioma
   * @param lastUpdateTime (Opcional) fecha de última actualización
   * @return cantidad de cadenas hoteleras cargadas
   */
  Integer loadChains(int from, int to, String language, String lastUpdateTime);
}

