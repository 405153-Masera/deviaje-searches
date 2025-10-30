package masera.deviajesearches.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa una cadena de alojamiento.
 */
@Entity
@Table(name = "chains")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chain {

  @Id
  private String code;

  private String description;
}