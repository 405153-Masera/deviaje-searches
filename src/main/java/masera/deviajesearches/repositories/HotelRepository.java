package masera.deviajesearches.repositories;

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
}
