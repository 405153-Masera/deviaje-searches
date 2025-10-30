package masera.deviajesearches.entities;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa la clave primaria compuesta de la entidad FacilityEntity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityId implements Serializable {

  private Integer code;

  private Integer facilityGroupCode;
}
