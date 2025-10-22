package masera.deviajesearches.dtos.amadeus.response.hotelbeds;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa un punto de interés.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterestPointDto {

  private Integer facilityCode;

  private Integer facilityGroupCode;

  private Integer order;

  private String poiName;

  private String distance;

  // Indica si hay una tarifa asociada al punto de interés
  private Boolean fee;
}
