package masera.deviajesearches.repositories;

import masera.deviajesearches.entities.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interfaz que define los métodos para acceder a los datos de los destinos.
 */
@Repository
public interface DestinationRepository extends JpaRepository<Destination, String> {
}
