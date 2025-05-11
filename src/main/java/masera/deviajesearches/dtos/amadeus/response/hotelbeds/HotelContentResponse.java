package masera.deviajesearches.dtos.amadeus.response.hotelbeds;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Data;

/**
 * DTO que representa la respuesta de contenido de hoteles.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HotelContentResponse {
  private List<HotelDto> hotels;
}

