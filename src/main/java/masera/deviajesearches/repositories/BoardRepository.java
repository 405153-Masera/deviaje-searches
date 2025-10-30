package masera.deviajesearches.repositories;

import masera.deviajesearches.entities.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interfaz que define los métodos para acceder a los datos de los regímenes de alimentos.
 */
@Repository
public interface BoardRepository extends JpaRepository<Board, String> {
}
