package masera.deviajesearches.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masera.deviajesearches.clients.HotelClient;
import masera.deviajesearches.dtos.amadeus.request.HotelSearchRequest;
import masera.deviajesearches.dtos.amadeus.response.CountryDto;
import masera.deviajesearches.dtos.amadeus.response.HotelResponseDto;
import masera.deviajesearches.dtos.amadeus.response.HotelSearchResponse;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.ContentDto;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.FacilityDto;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.ImageDto;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.InterestPointDto;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.IssueDto;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.PhoneDto;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.RoomDto;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.TerminalDto;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.WildcardDto;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.segments.SegmentDto;
import masera.deviajesearches.entities.FacilityId;
import masera.deviajesearches.entities.Hotel;
import masera.deviajesearches.entities.Segment;
import masera.deviajesearches.repositories.AccommodationRepository;
import masera.deviajesearches.repositories.CategoryRepository;
import masera.deviajesearches.repositories.ChainRepository;
import masera.deviajesearches.repositories.CountryRepository;
import masera.deviajesearches.repositories.DestinationRepository;
import masera.deviajesearches.repositories.FacilityGroupRepository;
import masera.deviajesearches.repositories.FacilityRepository;
import masera.deviajesearches.repositories.HotelRepository;
import masera.deviajesearches.repositories.SegmentRepository;
import masera.deviajesearches.services.interfaces.HotelSearchService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/**
 * Implementación del servicio de búsqueda de hoteles.
 * Esta clase se encarga de la lógica de negocio relacionada con la búsqueda de hoteles.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class HotelSearchServiceImpl implements HotelSearchService {

  private final HotelClient hotelClient;

  private final HotelRepository hotelRepository;

  private final CountryRepository countryRepository;

  private final DestinationRepository destinationRepository;

  private final CategoryRepository categoryRepository;

  private final ChainRepository chainRepository;

  private final AccommodationRepository accommodationRepository;

  private final SegmentRepository segmentRepository;

  private final FacilityRepository facilityRepository;

  private final FacilityGroupRepository facilityGroupRepository;

  private final ModelMapper modelMapper;

  private final ObjectMapper objectMapper;

  @Override
  public HotelSearchResponse searchHotels(HotelSearchRequest request) {
    log.info("Buscando hoteles con los siguientes parámetros: {}", request);
    return hotelClient.searchHotels(request).block();
  }

  @Override
  public HotelResponseDto getHotelDetails(String hotelCode) {
    log.info("Obteniendo detalles del hotel con código: {}", hotelCode);
    Optional<Hotel> optionalHotel = hotelRepository.findByCode(hotelCode);

    if (optionalHotel.isPresent()) {
      Hotel hotel = optionalHotel.get();
      HotelResponseDto responseDto = HotelResponseDto.builder()
              .code(hotel.getCode())
              .name(hotel.getName())
              .description(hotel.getDescription())
              .stateCode(hotel.getStateCode())
              .address(hotel.getAddress())
              .city(hotel.getCity())
              .zoneCode(hotel.getZoneCode())
              .email(hotel.getEmail())
              .web(hotel.getWeb())
              .ranking(hotel.getRanking())
              .s2c(hotel.getS2c())
              .build();
      enrichWithRelatedEntities(hotel, responseDto);
      processJsonFields(hotel, responseDto);

      return responseDto;
    } else {
      throw new EntityNotFoundException("Hotel no encontrado con código: " + hotelCode);
    }
  }

  /**
   * Enriquece el DTO con las entidades relacionadas desde la base de datos.
   *
   * @param hotel entidad Hotel
   * @param responseDto DTO de respuesta
   */
  private void enrichWithRelatedEntities(Hotel hotel, HotelResponseDto responseDto) {
    if (hotel.getCountryCode() != null) {
      countryRepository.findById(hotel.getCountryCode()).ifPresent(country ->
              responseDto.setCountry(new CountryDto(
                      country.getCode(),
                      country.getName(),
                      country.getIsoCode()
              ))
      );
    }

    if (hotel.getDestinationCode() != null) {
      destinationRepository.findById(hotel.getDestinationCode()).ifPresent(destination ->
              responseDto.setDestination(destination.getName())
      );
    }

    if (hotel.getCategoryCode() != null) {
      categoryRepository.findById(hotel.getCategoryCode()).ifPresent(category ->
              responseDto.setCategory(category.getDescription())
      );
    }

    if (hotel.getChainCode() != null) {
      chainRepository.findById(hotel.getChainCode()).ifPresent(chain ->
              responseDto.setChain(chain.getDescription())
      );
    }

    if (hotel.getAccommodationTypeCode() != null) {
      accommodationRepository.findById(hotel.getAccommodationTypeCode()).ifPresent(accommodation ->
              responseDto.setAccommodationType(accommodation.getTypeDescription())
      );
    }
  }

  /**
   * Procesa los campos JSON de la entidad Hotel para el DTO.
   *
   * @param hotel entidad Hotel
   * @param responseDto DTO de respuesta
   */
  private void processJsonFields(Hotel hotel, HotelResponseDto responseDto) {
    try {
      if (hotel.getImages() != null && !hotel.getImages().isEmpty()) {
        List<ImageDto> images = objectMapper.readValue(
                hotel.getImages(),
                new TypeReference<>() {}
        );
        responseDto.setImages(images);
      } else {
        responseDto.setImages(new ArrayList<>());
      }

      if (hotel.getFacilities() != null && !hotel.getFacilities().isEmpty()) {
        List<FacilityDto> facilities = objectMapper.readValue(
                hotel.getFacilities(),
                new TypeReference<>() {}
        );

        enrichFacilities(facilities);
        responseDto.setFacilities(facilities);
      }

      if (hotel.getRooms() != null && !hotel.getRooms().isEmpty()) {
        List<RoomDto> rooms = objectMapper.readValue(
                hotel.getRooms(),
                new TypeReference<>() {}
        );

        for (RoomDto room : rooms) {
          if (room.getRoomFacilities() != null && !room.getRoomFacilities().isEmpty()) {
            enrichFacilities(room.getRoomFacilities());
          }
        }

        responseDto.setRooms(rooms);
      }

      if (hotel.getPhones() != null && !hotel.getPhones().isEmpty()) {
        List<PhoneDto> phones = objectMapper.readValue(
                hotel.getPhones(),
                new TypeReference<>() {}
        );
        responseDto.setPhones(phones);
      } else {
        responseDto.setPhones(new ArrayList<>());
      }

      if (hotel.getInterestPoints() != null && !hotel.getInterestPoints().isEmpty()) {
        List<InterestPointDto> interestPoints = objectMapper.readValue(
                hotel.getInterestPoints(),
                new TypeReference<>() {}
        );
        responseDto.setInterestPoints(interestPoints);
      } else {
        responseDto.setInterestPoints(new ArrayList<>());
      }

      if (hotel.getSegmentCodes() != null && !hotel.getSegmentCodes().isEmpty()) {
        List<Integer> segmentCodes = objectMapper.readValue(
                hotel.getSegmentCodes(),
                new TypeReference<>() {}
        );

        List<SegmentDto> segments = new ArrayList<>();
        for (Integer code : segmentCodes) {
          Segment segment = segmentRepository.findById(code).orElse(null);
          SegmentDto segmentDto = modelMapper.map(segment, SegmentDto.class);
          segments.add(segmentDto);
        }
        responseDto.setSegments(segments);
      } else {
        responseDto.setSegments(new ArrayList<>());
      }

      if (hotel.getTerminals() != null && !hotel.getTerminals().isEmpty()) {
        List<TerminalDto> terminals = objectMapper.readValue(
                hotel.getTerminals(),
                new TypeReference<>() {}
        );
        responseDto.setTerminals(terminals);
      } else {
        responseDto.setTerminals(new ArrayList<>());
      }

      if (hotel.getWildcards() != null && !hotel.getWildcards().isEmpty()) {
        List<WildcardDto> wildcards = objectMapper.readValue(
                hotel.getWildcards(),
                new TypeReference<>() {}
        );
        responseDto.setWildcards(wildcards);
      } else {
        responseDto.setWildcards(new ArrayList<>());
      }

      if (hotel.getIssues() != null && !hotel.getIssues().isEmpty()) {
        List<IssueDto> issues = objectMapper.readValue(
                hotel.getIssues(),
                new TypeReference<>() {}
        );
        responseDto.setIssues(issues);
      } else {
        responseDto.setIssues(new ArrayList<>());
      }

    } catch (JsonProcessingException e) {
      log.error("Error al procesar campos JSON del hotel {}: {}",
              hotel.getCode(), e.getMessage(), e);
    }
  }

  /**
   * Enriquece las facilities con nombres desde las entidades Facility y FacilityGroup.
   *
   * @param facilities lista de facilities a enriquecer
   */
  private void enrichFacilities(List<FacilityDto> facilities) {
    for (FacilityDto facilityDto : facilities) {

      if (facilityDto.getFacilityCode() != null && facilityDto.getFacilityGroupCode() != null) {

        facilityRepository.findById(new FacilityId(
                facilityDto.getFacilityCode(),
                facilityDto.getFacilityGroupCode()
        )).ifPresent(facility -> {
          facilityDto.setDescription(new ContentDto());
          facilityDto.getDescription().setContent(facility.getDescription());

          facilityGroupRepository
                  .findById(facilityDto.getFacilityGroupCode())
                  .ifPresent(facilityGroup -> {
                    facilityDto.setFacilityGroupName(facilityGroup.getDescription());
                    facilityDto.setFacilityGroupCode(facilityGroup.getCode());
                  });
        });
      }
    }
  }
}
