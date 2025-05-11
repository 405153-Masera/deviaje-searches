package masera.deviajesearches.repositories;

import masera.deviajesearches.entities.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interfaz que define los métodos para acceder a los datos de los estados.
 */
@Repository
public interface StateRepository extends JpaRepository<State, Long> {
}
