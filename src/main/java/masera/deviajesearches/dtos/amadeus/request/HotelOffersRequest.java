package masera.deviajesearches.dtos.amadeus.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa la solicitud para obtener ofertas de hoteles.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelOffersRequest {

  @NotNull
  private List<String> hotelIds;

  private LocalDate checkInDate;

  private LocalDate checkOutDate;

  private Integer adults;

  private Integer roomQuantity;

  private String currency;

  private String priceRange;

  private List<String> boardType; // Tipo de pensión (opcional)
}
