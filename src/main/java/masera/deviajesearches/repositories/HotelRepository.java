package masera.deviajesearches.repositories;

import java.util.List;
import java.util.Optional;
import masera.deviajesearches.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interfaz que define los métodos para acceder a los datos de los hoteles.
 */
@Repository
public interface HotelRepository extends JpaRepository<Hotel, String> {

  /**
   * Busca un hotel por su código.
   *
   * @param code el código del hotel
   * @return un Optional que contiene el hotel si se encuentra, o vacío si no se encuentra
   */
  Optional<Hotel> findByCode(String code);

  /**
   * Busca una lista de hoteles por su código de destino.
   *
   * @param destinationCode el código de destino
   * @return una lista de hoteles que coinciden con el código de destino
   */
  List<Hotel> findByDestinationCode(String destinationCode);
}
