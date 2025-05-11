package masera.deviajesearches.repositories;

import java.util.Optional;
import masera.deviajesearches.entities.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interfaz que define los métodos para acceder a los datos de los estados.
 */
@Repository
public interface StateRepository extends JpaRepository<State, Integer> {
  /**
   * Busca un estado por su código.
   *
   * @param code el código del estado
   * @return un Optional que contiene el estado si se encuentra, o vacío si no
   */
  Optional<State> findByCode(String code);
}
