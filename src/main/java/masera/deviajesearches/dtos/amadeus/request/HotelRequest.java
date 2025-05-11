package masera.deviajesearches.dtos.amadeus.request;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * DTO que representa la solicitud para buscar tarifas de hoteles.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelRequest {

  @NotBlank
  private String cityCode;

  private Integer radius; //Radia de búsqueda con respecto a la ciudad

  private String radiusUnit; //Unidad de medida del radio

  private List<String> ratings; //Las estrellas de los hoteles

  private List<String> amenities; //Las comodidades de los hoteles

  private List<String> chainCodes; //Filtra por cadena hotelera
}
