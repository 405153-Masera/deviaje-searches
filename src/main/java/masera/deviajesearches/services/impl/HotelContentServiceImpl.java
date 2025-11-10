package masera.deviajesearches.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masera.deviajesearches.clients.HotelClient;
import masera.deviajesearches.dtos.amadeus.response.CityDto;
import masera.deviajesearches.dtos.amadeus.response.CountryDto;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.AccommodationTypeDto;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.BoardDto;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.CategoryDto;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.ChainDto;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.CountriesResponse;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.HotelContentResponse;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.HotelDto;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.accommodations.AccommodationResponse;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.boards.BoardsResponse;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.categories.CategoriesResponse;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.chains.ChainsResponse;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.destinations.DestinationsResponse;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.facilities.FacilitiesResponse;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.facilities.FacilityGroupsResponse;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.terminals.TerminalsResponse;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.terminals.TerminalsResponse.TerminalData;
import masera.deviajesearches.entities.Accommodation;
import masera.deviajesearches.entities.Board;
import masera.deviajesearches.entities.Category;
import masera.deviajesearches.entities.Chain;
import masera.deviajesearches.entities.Country;
import masera.deviajesearches.entities.Destination;
import masera.deviajesearches.entities.Facility;
import masera.deviajesearches.entities.FacilityGroup;
import masera.deviajesearches.entities.Hotel;
import masera.deviajesearches.entities.Terminal;
import masera.deviajesearches.repositories.AccommodationRepository;
import masera.deviajesearches.repositories.BoardRepository;
import masera.deviajesearches.repositories.CategoryRepository;
import masera.deviajesearches.repositories.ChainRepository;
import masera.deviajesearches.repositories.CountryRepository;
import masera.deviajesearches.repositories.DestinationRepository;
import masera.deviajesearches.repositories.FacilityGroupRepository;
import masera.deviajesearches.repositories.FacilityRepository;
import masera.deviajesearches.repositories.HotelRepository;
import masera.deviajesearches.repositories.TerminalRepository;
import masera.deviajesearches.services.interfaces.HotelContentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Implementación del servicio de contenido de hoteles.
 * Esta clase se encarga de la lógica de negocio relacionada con
 * la carga y actualización de hoteles así como la gestión de países y destinos.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class HotelContentServiceImpl implements HotelContentService {

  private final HotelClient hotelClient;

  private final ObjectMapper objectMapper;

  private final ModelMapper modelMapper;

  private final HotelRepository hotelRepository;

  private final CountryRepository countryRepository;

  private final DestinationRepository destinationRepository;

  private final AccommodationRepository accommodationRepository;

  private final BoardRepository boardRepository;

  private final CategoryRepository categoryRepository;

  private final FacilityRepository facilityRepository;

  private final FacilityGroupRepository facilityGroupRepository;

  private final ChainRepository chainRepository;

  private final TerminalRepository terminalRepository;

  /**
   * Carga hoteles desde la API de Hotelbeds.
   *
   * @param from índice inicial
   * @param to índice final
   * @param language idioma
   * @return lista de hoteles cargados
   */
  @Override
  public Integer loadHotels(int from, int to, String language, String lastUpdateTime) {
    log.info("Cargando hoteles desde {} hasta {} en idioma {}", from, to, language);

    HotelContentResponse response = hotelClient
            .getHotelContent(from, to, language, lastUpdateTime).block();
    return saveHotels(response);
  }

  @Override
  public List<CountryDto> getAllCountries() {
    log.info("Obteniendo todos los países");

    List<Country> countries = countryRepository.findAll();
    List<CountryDto> countryDtos = new ArrayList<>();

    for (Country country : countries) {
      countryDtos.add(new CountryDto(
              country.getCode(), country.getName(), country.getIsoCode()));
    }

    return countryDtos;
  }

  /**
   * Busca destinos por nombre.
   *
   * @param keyword palabra clave para buscar destinos
   * @return lista de destinos que coinciden con la palabra clave.
   */
  @Override
  public List<CityDto> searchDestinations(String keyword) {
    log.info("Buscando destinos con keyword: {}", keyword);
    List<Destination> destinations = destinationRepository
            .findByNameContainingIgnoreCaseWithCountry(keyword);
    return destinations.stream()
            .map(this::mapDestinationToCityDto)
            .collect(Collectors.toList());
  }

  private CityDto mapDestinationToCityDto(Destination destination) {
    CityDto cityDto = new CityDto();
    cityDto.setName(destination.getName());
    cityDto.setIataCode(destination.getCode());
    cityDto.setCountry(destination.getCountry() != null
            ? destination.getCountry().getName() : "País no especificado");
    return cityDto;
  }

  @Override
  public Integer loadCountries(
          int from, int to, String language, String lastUpdateTime) {

    log.info("Cargando países en idioma {}", language);
    CountriesResponse response = hotelClient.getCountries(
            from, to, language, lastUpdateTime).block();
    return processCountryResponse(response).size();
  }

  @Override
  public Integer loadDestinations(
          int from, int to, String language, String lastUpdateTime) {
    log.info("Cargando destinos en idioma {}", language);

    DestinationsResponse response = hotelClient.getDestinations(
            from, to, language, lastUpdateTime).block();
    return processDestinationResponse(response);
  }

  @Override
  public Integer loadAccommodations(int from, int to, String language, String lastUpdateTime) {
    log.info("Cargando tipos de alojamientos en idioma {}", language);

    AccommodationResponse response = hotelClient.getAccommodations(
            from, to, language, lastUpdateTime).block();
    return processAccommodationResponse(response);
  }


  @Override
  public Integer loadBoards(int from, int to, String language, String lastUpdateTime) {
    log.info("Cargando regímenes de alimentos en idioma {}", language);
    BoardsResponse response = hotelClient.getBoards(
            from, to, language, lastUpdateTime).block();

    return processBoardsResponse(response);
  }

  @Override
  public Integer loadCategories(int from, int to, String language, String lastUpdateTime) {
    log.info("Cargando categorías de hoteles en idioma {}", language);

    CategoriesResponse response = hotelClient.getCategories(
            from, to, language, lastUpdateTime).block();
    return processCategoriesResponse(response);
  }

  @Override
  public Integer loadFacilities(int from, int to, String language, String lastUpdateTime) {
    log.info("Cargando instalaciones en idioma {}", language);

    FacilitiesResponse response = hotelClient.getFacilities(
            from, to, language, lastUpdateTime).block();
    return processFacilitiesResponse(response);
  }



  @Override
  public Integer loadFacilityGroups(int from, int to, String language, String lastUpdateTime) {
    log.info("Cargando grupos de instalaciones en idioma {}", language);

    FacilityGroupsResponse response = hotelClient.getFacilityGroups(
            from, to, language, lastUpdateTime).block();
    return processFacilityGroupsResponse(response);
  }

  @Override
  public Integer loadChains(int from, int to, String language, String lastUpdateTime) {
    log.info("Cargando cadenas hoteleras en idioma {}", language);

    ChainsResponse response = hotelClient.getChains(from, to, language, lastUpdateTime).block();
    return processChainsResponse(response);
  }

  @Override
  public Integer loadTerminals(int from, int to, String language, String lastUpdateTime) {
    log.info("Cargando terminales en idioma {}", language);

    TerminalsResponse response = hotelClient.getTerminals(
            from, to, language, lastUpdateTime).block();
    return processTerminalsResponse(response);
  }

  /**
   *  Guarda los datos de los hoteles en la base de datos.
   *
   * @param response respuesta de la API
   * @return lista hoteles guardados
   */
  private Integer saveHotels(HotelContentResponse response) {
    List<Object> results = new ArrayList<>();

    if (response != null && response.getHotels() != null) {
      for (HotelDto hotelDto : response.getHotels()) {
        Hotel hotel = saveHotel(hotelDto);
        results.add(hotel);
      }
    }
    log.info("Procesados {} hoteles", results.size());
    return results.size();
  }

  /**
   * Procesa la respuesta de instalaciones de la API y los guarda en la base de datos.
   *
   * @param response respuesta de la API
   * @return número de instalaciones guardadas.
   */
  private Integer processFacilitiesResponse(FacilitiesResponse response) {

    int savedCount = 0;

    if (response != null && response.getFacilities() != null) {

      for (FacilitiesResponse.FacilityContent facilityData : response.getFacilities()) {
        Facility facility = new Facility();
        facility.setCode(facilityData.getCode());
        facility.setFacilityGroupCode(facilityData.getFacilityGroupCode());
        facility.setFacilityTypologyCode(facilityData.getFacilityTypologyCode());
        if (facilityData.getDescription() != null) {
          facility.setDescription(facilityData.getDescription().getContent());
        }
        facilityRepository.save(facility);
        savedCount++;
      }
    }
    log.info("Procesadas {} instalaciones", savedCount);
    return savedCount;
  }

  /**
   * Procesa la respuesta de grupos de instalaciones de la API y los guarda en la base de datos.
   *
   * @param response respuesta de la API
   * @return número de grupos de instalaciones guardados.
   */
  private Integer processFacilityGroupsResponse(FacilityGroupsResponse response) {
    int savedCount = 0;

    if (response != null && response.getFacilityGroups() != null) {
      for (FacilityGroupsResponse.FacilityGroupContent groupData : response.getFacilityGroups()) {

        FacilityGroup facilityGroup = new FacilityGroup();
        facilityGroup.setCode(groupData.getCode());
        if (groupData.getDescription() != null) {
          facilityGroup.setDescription(groupData.getDescription().getContent());
        }
        facilityGroupRepository.save(facilityGroup);
        savedCount++;
      }
    }
    log.info("Procesados {} grupos de instalaciones", savedCount);
    return savedCount;
  }

  /**
   * Procesa la respuesta de grupos de regímenes de alimentos y los guarda en la base de datos.
   *
   * @param response respuesta de la API
   * @return número de regímenes de alimentos guardados.
   */
  private int processBoardsResponse(BoardsResponse response) {

    int savedCount = 0;

    if (response != null && response.getBoards() != null) {
      for (BoardDto boardData : response.getBoards()) {

        Board board = new Board();
        board.setCode(boardData.getCode());
        board.setMultiLingualCode(boardData.getMultiLingualCode());
        if (boardData.getDescription() != null) {
          board.setDescription(boardData.getDescription().getContent());
        }
        boardRepository.save(board);
        savedCount++;
      }
    }
    log.info("Procesados {} regímenes de alimentos", savedCount);
    return savedCount;
  }

  /**
   * Procesa la respuesta de categorías de hoteles de la API y los guarda en la base de datos.
   *
   * @param response respuesta de la API
   * @return número de categorías guardadas
   */
  private int processCategoriesResponse(CategoriesResponse response) {

    int savedCount = 0;

    if (response != null && response.getCategories() != null) {
      for (CategoryDto categoryData : response.getCategories()) {

        Category category = new masera.deviajesearches.entities.Category();
        category.setCode(categoryData.getCode());
        if (categoryData.getDescription() != null) {
          category.setDescription(categoryData.getDescription().getContent());
        }
        category.setAccommodationType(categoryData.getAccommodationType());
        category.setCategoryGroup(categoryData.getGroup());
        category.setSimpleCode(categoryData.getSimpleCode());
        categoryRepository.save(category);
        savedCount++;
      }
    }
    log.info("Procesadas {} categorías de hoteles", savedCount);
    return savedCount;
  }

  /**
   * Procesa la respuesta de países de la API y los guarda en la base de datos.
   *
   * @param response respuesta de la API
   * @return número de países guardados
   */
  private List<CountryDto> processCountryResponse(CountriesResponse response) {
    List<CountryDto> countryDtos = new ArrayList<>();

    if (response != null && response.getCountries() != null) {
      for (CountriesResponse.CountryContent countryData : response.getCountries()) {
        String code = countryData.getCode();

        if (code != null) {
          Country country = new Country();
          country.setCode(code);
          country.setIsoCode(countryData.getIsoCode());

          if (countryData.getDescription() != null
                  && countryData.getDescription().getContent() != null) {
            country.setName(countryData.getDescription().getContent());
          }

          if (countryData.getStates() != null) {
            try {
              country.setStates(objectMapper.writeValueAsString(countryData.getStates()));
            } catch (Exception e) {
              throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                      "Error al procesar campos JSON: " + e.getMessage(), e);
            }
          }
          country = countryRepository.save(country);
          countryDtos.add(modelMapper.map(country, CountryDto.class));
        }
      }
    }
    log.info("Procesados {} países", countryDtos.size());
    return countryDtos;
  }

  private int processAccommodationResponse(AccommodationResponse response) {

    int savedCount = 0;

    if (response != null && response.getAccommodations() != null) {
      for (AccommodationTypeDto accommodationData : response.getAccommodations()) {

        Accommodation accommodation = new Accommodation();
        accommodation.setCode(accommodationData.getCode());
        accommodation.setTypeDescription(accommodationData.getTypeDescription());
        accommodationRepository.save(accommodation);
        savedCount++;
      }
    }
    log.info("Procesados {} tipos de alojamiento", savedCount);
    return savedCount;
  }

  /**
   * Procesa la respuesta de destinos de la API y los guarda en la base de datos.
   *
   * @param response respuesta de la API
   * @return número de destinos guardados
   */
  private int processDestinationResponse(DestinationsResponse response) {
    int savedCount = 0;

    if (response != null && response.getDestinations() != null) {
      for (DestinationsResponse
              .DestinationContent destinationData : response.getDestinations()) {

        String countryCode = destinationData.getCountryCode();
        Destination destination = new Destination();
        destination.setCode(destinationData.getCode());

        if (countryCode != null) {
          countryRepository.findById(countryCode).ifPresent(destination::setCountry);
        }

        if (destinationData.getName() != null) {
          destination.setName(destinationData.getName().getContent());
        }
        destinationRepository.save(destination);
        savedCount++;
      }
    }
    log.info("Procesados {} destinos", savedCount);
    return savedCount;
  }

  /**
   * Procesa la respuesta de cadenas hoteleras de la API y los guarda en la base de datos.
   *
   * @param response respuesta de la API
   * @return número de cadenas hoteleras guardadas
   */
  private Integer processChainsResponse(ChainsResponse response) {
    int savedCount = 0;

    if (response != null && response.getChains() != null) {
      for (ChainDto chainData : response.getChains()) {

        Chain chain = new masera.deviajesearches.entities.Chain();
        chain.setCode(chainData.getCode());
        if (chainData.getDescription() != null) {
          chain.setDescription(chainData.getDescription().getContent());
        }
        chainRepository.save(chain);
        savedCount++;
      }
    }
    log.info("Procesadas {} cadenas hoteleras", savedCount);
    return savedCount;
  }

  private Integer processTerminalsResponse(TerminalsResponse response) {
    int savedCount = 0;

    if (response != null && response.getTerminals() != null) {
      for (TerminalData terminalData : response.getTerminals()) {

        Terminal terminal = new Terminal();
        terminal.setCode(terminalData.getCode());
        terminal.setType(terminalData.getType());
        terminal.setCountry(terminalData.getCountry());
        terminal.setLanguageCode(terminalData.getName().getLanguageCode());

        if (terminalData.getName() != null) {
          terminal.setName(terminalData.getName().getContent());
        }
        if (terminalData.getDescription() != null) {
          terminal.setDescription(terminalData.getDescription().getContent());
        }
        terminalRepository.save(terminal);
        savedCount++;
      }
    }
    log.info("Procesadas {} terminales", savedCount);
    return savedCount;
  }

  /**
   * Guarda un hotel en la base de datos.
   *
   * @param hotelDto datos del hotel
   * @return hotel guardado
   */
  private Hotel saveHotel(HotelDto hotelDto) {

    Optional<Hotel> existingHotel = hotelRepository.findById(hotelDto.getCode());

    Hotel hotel = existingHotel.orElse(new Hotel());
    hotel.setCode(hotelDto.getCode());
    hotel.setName(hotelDto.getName().getContent());
    hotel.setCountryCode(hotelDto.getCountryCode());
    hotel.setStateCode(hotelDto.getStateCode());
    hotel.setDestinationCode(hotelDto.getDestinationCode());
    hotel.setZoneCode(hotelDto.getZoneCode());

    if (hotelDto.getDescription() != null) {
      hotel.setDescription(hotelDto.getDescription().getContent());
    }

    if (hotelDto.getCoordinates() != null) {
      hotel.setLatitude(hotelDto.getCoordinates().getLatitude());
      hotel.setLongitude(hotelDto.getCoordinates().getLongitude());
    }

    hotel.setGiataCode(hotelDto.getGiataCode());
    hotel.setCategoryCode(hotelDto.getCategoryCode());
    hotel.setCategoryGroupCode(hotelDto.getCategoryGroupCode());
    hotel.setChainCode(hotelDto.getChainCode());
    hotel.setAccommodationTypeCode(hotelDto.getAccommodationTypeCode());

    if (hotelDto.getAddress() != null) {
      hotel.setAddress(hotelDto.getAddress().getContent());
    }

    hotel.setCity(hotelDto.getCity() != null
            ? hotelDto.getCity().getContent() : null);
    hotel.setPostalCode(hotelDto.getPostalCode());
    hotel.setEmail(hotelDto.getEmail());
    hotel.setLicense(hotelDto.getLicense());

    // Guardar campos JSON
    try {
      if (hotelDto.getPhones() != null && !hotelDto.getPhones().isEmpty()) {
        hotel.setPhones(objectMapper.writeValueAsString(hotelDto.getPhones()));
      }

      if (hotelDto.getBoardCodes() != null) {
        hotel.setBoardCodes(objectMapper.writeValueAsString(hotelDto.getBoardCodes()));
      }

      if (hotelDto.getSegmentCodes() != null && !hotelDto.getSegmentCodes().isEmpty()) {
        hotel.setSegmentCodes(objectMapper.writeValueAsString(hotelDto.getSegmentCodes()));
      }

      if (hotelDto.getFacilities() != null) {
        hotel.setFacilities(objectMapper.writeValueAsString(hotelDto.getFacilities()));
      }

      if (hotelDto.getTerminals() != null) {
        hotel.setTerminals(objectMapper.writeValueAsString(hotelDto.getTerminals()));
      }

      if (hotelDto.getImages() != null) {
        hotel.setImages(objectMapper.writeValueAsString(hotelDto.getImages()));
      }

      if (hotelDto.getRooms() != null) {
        hotel.setRooms(objectMapper.writeValueAsString(hotelDto.getRooms()));
      }

      if (hotelDto.getInterestPoints() != null && !hotelDto.getInterestPoints().isEmpty()) {
        hotel.setInterestPoints(objectMapper.writeValueAsString(hotelDto.getInterestPoints()));
      }

      if (hotelDto.getWildcards() != null && !hotelDto.getWildcards().isEmpty()) {
        hotel.setWildcards(objectMapper.writeValueAsString(hotelDto.getWildcards()));
      }

      if (hotelDto.getIssues() != null && !hotelDto.getIssues().isEmpty()) {
        hotel.setIssues(objectMapper.writeValueAsString(hotelDto.getIssues()));
      }

    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
              "Error al procesar campos JSON: " + e.getMessage(), e);
    }

    hotel.setWeb(hotelDto.getWeb());
    hotel.setLastUpdate(hotelDto.getLastUpdate());
    hotel.setS2c(hotelDto.getS2C());
    hotel.setRanking(hotelDto.getRanking());
    hotel.setLastUpdated(LocalDateTime.now());
    return hotelRepository.save(hotel);
  }
}
