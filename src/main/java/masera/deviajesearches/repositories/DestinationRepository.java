package masera.deviajesearches.repositories;

import java.util.List;
import masera.deviajesearches.entities.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Interfaz que define los métodos para acceder a los datos de los destinos.
 */
@Repository
public interface DestinationRepository extends JpaRepository<Destination, String> {

  /**
   * Busca destinos por nombre, ignorando mayúsculas y minúsculas.
   *
   * @param keyword la palabra clave a buscar en el nombre del destino
   * @return una lista de destinos que coinciden con la palabra clave.
   */
  @Query("SELECT d FROM Destination d LEFT JOIN FETCH d.country WHERE LOWER(d.name) "
          + "LIKE LOWER(CONCAT('%', :keyword, '%'))")
  List<Destination> findByNameContainingIgnoreCaseWithCountry(@Param("keyword") String keyword);
}
