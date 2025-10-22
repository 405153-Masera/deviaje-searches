package masera.deviajesearches.dtos.amadeus.response.hotelbeds;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa una instalación de estancia en habitación.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomStayFacilityDto {

  private ContentDto description;

  private Integer facilityCode;

  private Integer facilityGroupCode;

  private Integer number;
}
