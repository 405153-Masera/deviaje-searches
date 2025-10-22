package masera.deviajesearches.dtos.amadeus.response.hotelbeds;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa una estancia en habitación.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomStayDto {

  private String stayType;

  private String order;

  private String description;

  private List<RoomStayFacilityDto> roomStayFacilities;
}
