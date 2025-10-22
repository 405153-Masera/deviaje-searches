package masera.deviajesearches.dtos.amadeus.response.hotelbeds;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa un número de teléfono.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneDto {

  private String phoneNumber;

  private String phoneType;
}
