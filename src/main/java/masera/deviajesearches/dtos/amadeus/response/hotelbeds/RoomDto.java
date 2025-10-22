package masera.deviajesearches.dtos.amadeus.response.hotelbeds;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa una habitación de un hotel.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto {

  private String roomCode;

  private Boolean isParentRoom;

  private Integer minPax;

  private Integer maxPax;

  private Integer maxAdults;

  private Integer maxChildren;

  private Integer minAdults;

  private String roomType;

  private String characteristicCode;

  private String description;

  private List<FacilityDto> roomFacilities;

  private List<RoomStayDto> roomStays;
}
