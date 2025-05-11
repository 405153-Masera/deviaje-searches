package masera.deviajesearches.repositories;

import java.util.Optional;
import masera.deviajesearches.entities.Destination;
import masera.deviajesearches.entities.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Zone.
 */
@Repository
public interface ZoneRepository extends JpaRepository<Zone, Integer> {

  /**
   * Busca una zona por su código y destino.
   *
   * @param zoneCode código de la zona
   * @param destination destino al que pertenece la zona
   * @return Optional que contiene la zona si se encuentra.
   */
  Optional<Zone> findByZoneCodeAndDestination(Integer zoneCode, Destination destination);


  /**
   * Busca una zona por su código.
   *
   * @param zoneCode código de la zona
   * @return opcional con la zona encontrada, o vacío si no se encuentra
   */
  Optional<Zone> findByZoneCodeAndDestinationCode(Integer zoneCode, String destinationCode);
}
