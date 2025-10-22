package masera.deviajesearches.dtos.amadeus.response.hotelbeds;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa una imagen de un hotel.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto {

  private String characteristicCode;

  private String imageTypeCode;

  private String path;

  private String roomCode;

  private String roomType;

  private Integer order;

  private String visualOrder;

  private String PMSRoomCode;
}
