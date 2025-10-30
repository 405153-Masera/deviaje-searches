package masera.deviajesearches.dtos.amadeus.response;

import java.math.BigDecimal;
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

  private HotelsResponse hotels;

  /**
   * Representa la lista de hoteles.
   */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class HotelsResponse {

    private List<Hotel> hotels;

    private String checkIn;

    private String checkOut;

    private Integer total;
  }

  /**
   * Representa un hotel con sus detalles.
   */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Hotel {

    private String code;

    private String name;

    private String categoryCode;

    private String categoryName;

    private String destinationCode;

    private String destinationName;

    private Integer zoneCode;

    private String zoneName;

    private Double latitude;

    private Double longitude;

    private List<Room> rooms;

    private BigDecimal minRate;

    private BigDecimal maxRate;

    private String currency;
  }

  /**
   * Representa una habitación de hotel con sus tarifas.
   */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Room {

    private String code;

    private String name;

    private List<Rate> rates;
  }

  /**
   * Representa una tarifa de habitación de hotel.
   */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Rate {

    private String rateKey;

    private String rateClass;

    private String rateType;

    private BigDecimal net;

    //Disponibilidad de la tarifa
    private Integer allotment;

    private String rateCommentsId;

    private String paymentType;

    private Boolean packaging;

    private String boardCode;

    private String boardName;

    private List<CancellationPolicy> cancellationPolicies;

    private Integer rooms;

    private Integer adults;

    private Integer children;

    private String childrenAges; // Edades separadas por comas "10,8"

    private List<Promotion> promotions;

    private List<Offer> offers;
  }

  /**
   * Representa una promoción aplicada a una tarifa.
   */
  @Data
  public static  class Promotion {

    private String code;

    private String name;

    private String remark;
  }

  /**
   * Representa una oferta aplicada a una tarifa.
   */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Offer {

    private String code;

    private String name;

    private BigDecimal amount;
  }

  /**
   * Representa la política de cancelación de una tarifa.
   */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CancellationPolicy {
    private BigDecimal amount;
    private String from;
  }
}