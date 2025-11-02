package masera.deviajesearches.dtos.amadeus.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.FacilityDto;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.ImageDto;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.InterestPointDto;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.IssueDto;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.PhoneDto;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.RoomDto;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.TerminalDto;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.WildcardDto;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.segments.SegmentDto;

/**
 * DTO que representa un hotel.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelResponseDto {

  private String code;

  private String name;

  private String description;

  private CountryDto country;

  private String stateCode;

  private String destination;

  private Integer zoneCode;

  private String category;

  private String chain;

  private String accommodationType;

  private String address;

  private String street;

  private String number;

  private String city;

  private String email;

  private String web;

  private List<PhoneDto> phones;

  private List<FacilityDto> facilities;

  private List<RoomDto> rooms;

  private List<ImageDto> images;

  private List<InterestPointDto> interestPoints;

  private List<SegmentDto> segments;

  private List<TerminalDto> terminals;

  private List<WildcardDto> wildcards;

  private List<IssueDto> issues;

  private Integer ranking;

  private String s2c;
}
