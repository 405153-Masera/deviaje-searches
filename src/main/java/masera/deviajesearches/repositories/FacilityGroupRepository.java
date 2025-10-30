package masera.deviajesearches.repositories;

import masera.deviajesearches.entities.FacilityGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la gestión de grupos de instalaciones.
 */
@Repository
public interface FacilityGroupRepository extends JpaRepository<FacilityGroup, Integer> {

}
