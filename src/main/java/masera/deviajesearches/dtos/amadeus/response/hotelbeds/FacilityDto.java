package masera.deviajesearches.dtos.amadeus.response.hotelbeds;

import lombok.Data;

@Data
public class FacilityDto {
  private Integer facilityCode;
  private Integer facilityGroupCode;
  private Integer order;
  private Integer number;
  private Integer distance;
  private Boolean indLogic;
  private Boolean indFee;
  private Boolean voucher;
}
