package masera.deviajesearches.dtos.amadeus.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa un país.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CountryDto {

  private String code;
  private String name;
}
