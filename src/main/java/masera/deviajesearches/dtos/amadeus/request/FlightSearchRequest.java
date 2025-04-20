package masera.deviajesearches.dtos.amadeus.request;


import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa una solicitud de búsqueda de vuelos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightSearchRequest {

  private String origin;
  private String destination;
  private LocalDate departureDate;
  private LocalDate returnDate;
  private Integer adults;
  private Integer children;
  private Integer infants;
  private String travelClass; // ECONOMY, PREMIUM_ECONOMY, BUSINESS, FIRST
  private Integer maxResults;
  private String currency;
  private Boolean nonStop;
}
