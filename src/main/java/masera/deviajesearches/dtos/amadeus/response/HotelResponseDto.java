package masera.deviajesearches.dtos.amadeus.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa un hotel.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelResponseDto {

  private String code;
  private String name;
  private String description;
  private CountryDto country;
  private String state;
  private String destination;
  private String zone;
  private String categoryCode;
  private String categoryGroupCode;
  private String chainCode;
  private String accommodationTypeCode;
  private String address;
  private String street;
  private String number;
  private String city;
  private String email;
  private List<String> images;
}
