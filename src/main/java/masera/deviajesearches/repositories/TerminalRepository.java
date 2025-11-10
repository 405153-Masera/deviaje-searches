package masera.deviajesearches.repositories;

import masera.deviajesearches.entities.Terminal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interfaz que define los métodos para acceder a los datos de las terminales.
 */
@Repository
public interface TerminalRepository extends JpaRepository<Terminal, String> {

}