package masera.deviajesearches.dtos.amadeus.response.hotelbeds.segments;

import jakarta.persistence.Id;
import lombok.Data;

/**
 * Representa los segmentos del hotel.
 */
@Data
public class SegmentDto {

  @Id
  private Integer code;

  private String content;
}
