package masera.deviajesearches.dtos.amadeus.response.hotelbeds;

import lombok.Data;

@Data
public class ImageDto {
  private String imageTypeCode;
  private String path;
  private String roomCode;
  private String roomType;
  private Integer order;
  private String visualOrder;
}
