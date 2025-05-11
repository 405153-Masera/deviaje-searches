package masera.deviajesearches.dtos.amadeus.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa la respuesta de búsqueda de hoteles.
 * Contiene una lista de hoteles y sus detalles.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelSearchResponse {

  private HotelsWrapper hotels;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class HotelsWrapper {
    private List<Hotel> hotels;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Hotel {
    private String code;
    private String name;
    private String categoryCode;
    private String categoryName;
    private String destinationCode;
    private Double latitude;
    private Double longitude;
    private List<Room> rooms;
    private Double minRate;
    private String currency;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Room {
    private String code;
    private String name;
    private List<Rate> rates;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Rate {
    private String rateKey;
    private String rateClass;
    private String rateType;
    private Double net;
    private Double discount;
    private List<CancellationPolicy> cancellationPolicies;
    private String boardCode;
    private String boardName;
    private List<Tax> taxes;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CancellationPolicy {
    private String amount;
    private String from;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Tax {
    private Boolean included;
    private Double amount;
    private String currency;
  }
}