package masera.deviajesearches.repositories;

import masera.deviajesearches.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interfaz que define los métodos para acceder a los datos de las categorías.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

}
