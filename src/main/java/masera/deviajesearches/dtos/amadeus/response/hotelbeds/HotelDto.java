package masera.deviajesearches.dtos.amadeus.response.hotelbeds;

import java.util.List;
import lombok.Data;

/**
 * DTO que representa un hotel.
 */
@Data
public class HotelDto {
  private String code;
  private ContentDto name;
  private ContentDto description;
  private String countryCode;
  private String stateCode;
  private String destinationCode;
  private Integer zoneCode;
  private Coordinates coordinates;
  private String categoryCode;
  private String categoryGroupCode;
  private String chainCode;
  private String accommodationTypeCode;
  private List<String> boards;
  private List<Integer> segmentCodes;
  private AddressDto address;
  private String postalCode;
  private ContentDto city;
  private String email;
  private String license;
  private Integer giataCode;
  private List<PhoneDto> phones;
  private List<RoomDto> rooms;
  private List<FacilityDto> facilities;
  private List<ImageDto> images;
  private List<TerminalDto> terminals;
  private String web;
  private String lastUpdate;
  private String S2C;
  private Integer ranking;
}
