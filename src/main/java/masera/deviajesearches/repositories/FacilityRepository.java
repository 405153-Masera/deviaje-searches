package masera.deviajesearches.repositories;

import masera.deviajesearches.entities.Facility;
import masera.deviajesearches.entities.FacilityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la gestión de instalaciones.
 */
@Repository
public interface FacilityRepository extends JpaRepository<Facility, FacilityId> {

}
