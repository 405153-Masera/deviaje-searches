package masera.deviajesearches.dtos.amadeus.response.hotelbeds;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa las instalaciones de un hotel.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacilityDto {

  private Integer facilityCode;

  private Integer facilityGroupCode;

  private String facilityName;

  private Integer order;

  private Integer distance;

  //Define si la instalación está disponible en el establecimiento
  private Boolean indLogic;

  //Valor numérico asociado de la instalación
  private Integer number;

  //Define si la instalación tiene costo en el establecimiento
  private Boolean indFee;

  //Define si la instalación obligatoria existe en el establecimiento
  private Boolean indYesOrNo;

  //Indica si el uso de la instalación emite un voucher
  private Boolean voucher;

  // Maxímo y mínimo de edad para acceder a la instalación
  private Integer ageFrom;

  private Integer ageTo;

  // Tasa de la instalación
  private Double amount;

  // Tipo de solicitud de la tasa de la instalación
  private String applicationType;

  private String currency;

  // Fecha desde la que está disponible la instalación
  private String dateFrom;

  private String dateTo;

  private ContentDto description;

  // Horario de disponibilidad de la instalación
  private String timeFrom;

  private String timeTo;
}
