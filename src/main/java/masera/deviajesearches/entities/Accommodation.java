package masera.deviajesearches.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa un tipo de alojamiento.
 */
@Entity
@Table(name = "accommodations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Accommodation {

  @Id
  private String code;

  private String typeDescription;
}
