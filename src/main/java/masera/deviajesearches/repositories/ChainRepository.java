package masera.deviajesearches.repositories;

import masera.deviajesearches.entities.Chain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la gestión de cadenas.
 */
@Repository
public interface ChainRepository extends JpaRepository<Chain, String> {

}
