package masera.deviajesearches.repositories;

import masera.deviajesearches.entities.Segment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *  Interfaz que define los métodos para acceder a los datos de los segmentos.
 */
@Repository
public interface SegmentRepository extends JpaRepository<Segment, Integer> {

}
