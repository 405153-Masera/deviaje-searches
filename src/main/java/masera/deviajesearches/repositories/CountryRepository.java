package masera.deviajesearches.repositories;

import masera.deviajesearches.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interfaz que define los métodos para acceder a los datos de los países.
 */
@Repository
public interface CountryRepository extends JpaRepository<Country, String> {
}
