package masera.deviajesearches.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa una instalación o servicio ofrecido por un alojamiento.
 */
@Entity
@Table(name = "facilities")
@IdClass(FacilityId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Facility {

  @Id
  private Integer code;

  @Id
  private Integer facilityGroupCode;

  private Integer facilityTypologyCode;

  private String description;
}
