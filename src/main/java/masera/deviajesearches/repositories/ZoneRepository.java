package masera.deviajesearches.repositories;

import masera.deviajesearches.entities.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Zone.
 */
@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {
}
