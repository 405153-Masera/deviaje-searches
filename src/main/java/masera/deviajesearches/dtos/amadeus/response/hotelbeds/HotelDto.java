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

  // Clasificación del hotel (número de estrellas)
  private String categoryCode;

  // Nombre del grupo de categoría a la que pertenece el hotel
  private String categoryGroupCode;

  // Código de la cadena hotelera a la que pertenece el hotel
  private String chainCode;

  // Tipo de alojamiento (hotel, hostal, apartamento, etc.)
  private String accommodationTypeCode;

  // Lista de regímenes alimentarios ofrecidos por el hotel
  private List<String> boardCodes;

  private List<Integer> segmentCodes;

  private AddressDto address;

  private ContentDto city;

  private String email;

  private String license;

  private Integer giataCode;

  private List<PhoneDto> phones;

  private String postalCode;

  private List<RoomDto> rooms;

  // Lista de instalaciones y servicios ofrecidos por el hotel
  private List<FacilityDto> facilities;

  private List<ImageDto> images;

  private List<InterestPointDto> interestPoints;

  private List<IssueDto> issues;

  private List<TerminalDto> terminals;

  private String web;

  private String lastUpdate;

  private List<WildcardDto> wildcards;

  //Calificación de salud y seguridad
  @SuppressWarnings("checkstyle:MemberName")
  private String S2C;

  private Integer ranking;
}
