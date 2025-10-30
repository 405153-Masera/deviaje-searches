package masera.deviajesearches.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa un grupo de instalaciones o servicios ofrecidos por un alojamiento.
 */
@Entity
@Table(name = "facility_groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacilityGroup {

  @Id
  private Integer code;

  private String description;
}
