package masera.deviajesearches.dtos.amadeus.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa la solicitud para obtener ofertas de habitaciones
 * de hoteles.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelSearchRequest {

  /**
   * Representa la fecha de entrada y salida del hotel.
   */
  @NotNull
  private Stay stay;

  @NotNull
  private List<Occupancy> occupancies;

  @NotNull
  private Destination destination;

  private Filter filter;

  private Boolean dailyRate;

  private String language;

  private String currency;

  /**
   * Representa la fecha de entrada y salida del hotel.
   */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Stay {
    @NotNull
    private LocalDate checkIn;

    @NotNull
    private LocalDate checkOut;
  }

  /**
   * Representa la ocupación de una habitación en el hotel.
   */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Occupancy {
    @NotNull
    private Integer rooms;

    @NotNull
    private Integer adults;

    @NotNull
    private Integer children;

    private List<Pax> paxes;
  }

  /**
   * Representa un pasajero en la ocupación de una habitación.
   */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Pax {
    @NotNull
    private String type; // "AD" o "CH"

    private Integer age; // Requerido para niños
  }

  /**
   * Representa el destino de la búsqueda de hoteles.
   */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Destination {

    /**
     * Representa la ciudad de destino.
     */
    @NotNull
    private String code;

    /**
     * Opcional: Código de zona dentro del destino.
     */
    private String zoneCode;
  }

  /**
   * Representa los filtros para la búsqueda de hoteles.
   */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Filter {
    private Integer minCategory;
    private Integer maxCategory;
    private Double minRate;
    private Double maxRate;
  }
}
