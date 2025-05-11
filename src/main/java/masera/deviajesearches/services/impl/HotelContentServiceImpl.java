package masera.deviajesearches.services.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masera.deviajesearches.clients.HotelClient;
import masera.deviajesearches.dtos.amadeus.response.CountryDto;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.CountriesResponse;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.HotelContentResponse;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.HotelDto;
import masera.deviajesearches.entities.Country;
import masera.deviajesearches.entities.Destination;
import masera.deviajesearches.entities.Hotel;
import masera.deviajesearches.entities.State;
import masera.deviajesearches.entities.Zone;
import masera.deviajesearches.repositories.CountryRepository;
import masera.deviajesearches.repositories.DestinationRepository;
import masera.deviajesearches.repositories.HotelRepository;
import masera.deviajesearches.repositories.StateRepository;
import masera.deviajesearches.repositories.ZoneRepository;
import masera.deviajesearches.services.interfaces.HotelContentService;
import org.springframework.stereotype.Service;


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
  private final HotelRepository hotelRepository;
  private final CountryRepository countryRepository;
  private final StateRepository stateRepository;
  private final DestinationRepository destinationRepository;
  private final ZoneRepository zoneRepository;

  @Override
  public List<Object> loadHotels(int from, int to, String language) {
    log.info("Cargando hoteles desde {} hasta {} en idioma {}", from, to, language);

    try {
      HotelContentResponse response = hotelClient.getHotelContent(from, to, language).block();
      return saveHotels(response);
    } catch (Exception e) {
      log.error("Error al cargar hoteles: {}", e.getMessage(), e);
      throw new RuntimeException("Error al cargar hoteles", e);
    }
  }

  @Override
  public List<Object> updateHotels(int from, int to, String language, String lastUpdateTime) {
    log.info("Actualizando hoteles desde {} hasta {} en idioma {} desde {}",
            from, to, language, lastUpdateTime);

    try {
      HotelContentResponse response = hotelClient
              .getHotelContentUpdates(from, to, language, lastUpdateTime).block();
      return saveHotels(response);
    } catch (Exception e) {
      log.error("Error al actualizar hoteles: {}", e.getMessage(), e);
      throw new RuntimeException("Error al actualizar hoteles", e);
    }
  }

  @Override
  public List<CountryDto> getAllCountries() {
    log.info("Obteniendo todos los países");

    List<Country> countries = countryRepository.findAll();
    List<CountryDto> countryDtos = new ArrayList<>();

    for (Country country : countries) {
      countryDtos.add(new CountryDto(country.getCode(), country.getDescription()));
    }

    return countryDtos;
  }

  @Override
  public List<Destination> getAllDestinations() {
    log.info("Obteniendo todos los destinos");
    return destinationRepository.findAll();
  }

  @Override
  public Map<String, Object> loadCountries(String language) {
    log.info("Cargando países en idioma {}", language);
    Map<String, Object> result = new HashMap<>();

    try {
      CountriesResponse response = hotelClient.getCountries(language).block();
      int savedCount = processCountryResponse(response);

      result.put("status", "success");
      result.put("message", "Países cargados exitosamente");
      result.put("count", savedCount);

      return result;
    } catch (Exception e) {
      log.error("Error al cargar países: {}", e.getMessage(), e);

      result.put("status", "error");
      result.put("message", "Error al cargar países: " + e.getMessage());

      return result;
    }
  }

  @Override
  public Map<String, Object> loadDestinations(String countryCode, String language) {
    log.info("Cargando destinos para país {} en idioma {}", countryCode, language);
    Map<String, Object> result = new HashMap<>();

    try {
      Object response = hotelClient.getDestinations(countryCode, language).block();
      int savedCount = processDestinationResponse(response);

      result.put("status", "success");
      result.put("message", "Destinos cargados exitosamente");
      result.put("count", savedCount);

      return result;
    } catch (Exception e) {
      log.error("Error al cargar destinos: {}", e.getMessage(), e);

      result.put("status", "error");
      result.put("message", "Error al cargar destinos: " + e.getMessage());

      return result;
    }
  }

  /**
   *  Guarda los datos de los hoteles en la base de datos.
   *
   * @param response respuesta de la API
   * @return lista hoteles guardados
   */
  private List<Object> saveHotels(HotelContentResponse response) {
    List<Object> result = new ArrayList<>();

    try {
      if (response != null && response.getHotels() != null) {
        for (HotelDto hotelDto : response.getHotels()) {
          try {
            Country country = getCountry(hotelDto.getCountryCode());
            State state = getState(hotelDto.getStateCode());
            Destination destination = getDestination(hotelDto.getDestinationCode());
            Zone zone = getZone(hotelDto.getZoneCode());
            Hotel hotel = saveHotel(hotelDto, country, state, destination, zone);
            result.add(hotel);
          } catch (Exception e) {
            log.error("Error al procesar hotel {}: {}", hotelDto.getCode(), e.getMessage(), e);
          }
        }
      }
    } catch (Exception e) {
      log.error("Error al procesar respuesta de contenido de hoteles: {}", e.getMessage(), e);
    }

    log.info("Procesados {} hoteles", result.size());
    return result;
  }

  /**
   * Procesa la respuesta de países de la API y los guarda en la base de datos.
   *
   * @param response respuesta de la API
   * @return número de países guardados
   */
  private int processCountryResponse(CountriesResponse response) {
    int savedCount = 0;

    try {
      if (response != null && response.getCountries() != null) {
        for (CountriesResponse.CountryContent countryData : response.getCountries()) {
          try {
            String code = countryData.getCode();

            if (code != null) {
              Country country = countryRepository.findById(code)
                      .orElse(new Country());

              country.setCode(code);

              // Procesar descripción del país
              if (countryData.getDescription() != null &&
                      countryData.getDescription().getContent() != null) {
                country.setDescription(countryData.getDescription().getContent());
              } else {
                country.setDescription("País " + code);
              }

              // Guardar el país
              countryRepository.save(country);
              savedCount++;

              // También procesar estados si existen
              if (countryData.getStates() != null && !countryData.getStates().isEmpty()) {
                for (CountriesResponse.StateContent stateData : countryData.getStates()) {
                  processState(stateData, country);
                }
              }
            }

          } catch (Exception e) {
              log.error("Error al procesar país: {}", e.getMessage(), e);
          }
        }
      }
    } catch (Exception e) {
      log.error("Error al procesar respuesta de países: {}", e.getMessage(), e);
    }

    log.info("Procesados {} países", savedCount);
    return savedCount;
  }

  private void processState(CountriesResponse.StateContent stateData, Country country) {
    try {
      if (stateData.getCode() != null) {
        State state = stateRepository.findByCode(stateData.getCode())
                .orElse(new State());

        state.setCode(stateData.getCode());
        state.setCountry(country);

        if (stateData.getName() != null && stateData.getName().getContent() != null) {
          state.setName(stateData.getName().getContent());
        } else {
          state.setName("Estado " + stateData.getCode());
        }

        stateRepository.save(state);
      }
    } catch (Exception e) {
      log.error("Error al procesar estado: {}", e.getMessage(), e);
    }
  }

  /**
   * Procesa la respuesta de destinos de la API y los guarda en la base de datos.
   *
   * @param response respuesta de la API
   * @return número de destinos guardados
   */
  private int processDestinationResponse(Object response) {
    int savedCount = 0;

    try {
      if (response instanceof Map) {
        Map<String, Object> responseMap = (Map<String, Object>) response;

        if (responseMap.containsKey("destinations")
                && responseMap.get("destinations") instanceof List) {
          List<Map<String, Object>> destinations =
                  (List<Map<String, Object>>) responseMap.get("destinations");

          for (Map<String, Object> destinationData : destinations) {
            try {
              String code = (String) destinationData.get("code");
              String countryCode = (String) destinationData.get("countryCode");

              if (code != null) {
                Destination destination = destinationRepository.findById(code)
                        .orElse(new Destination());

                destination.setCode(code);

                // Procesar descripción del destino
                if (destinationData.containsKey("name")) {
                  if (destinationData.get("name") instanceof Map) {
                    Map<String, Object> nameMap = (Map<String, Object>) destinationData.get("name");
                    if (nameMap.containsKey("content")) {
                      destination.setName((String) nameMap.get("content"));
                    }
                  } else if (destinationData.get("name") instanceof String) {
                    destination.setName((String) destinationData.get("name"));
                  }
                }

                // Si no hay nombre, usar el código como nombre
                if (destination.getName() == null || destination.getName().isEmpty()) {
                  destination.setName("Destino " + code);
                }

                // Vincular con el país correspondiente
                if (countryCode != null) {
                  countryRepository.findById(countryCode).ifPresent(destination::setCountry);
                }

                // Guardar el destino
                destinationRepository.save(destination);
                savedCount++;

                // Procesar zonas si están presentes
                if (destinationData.containsKey("zones")
                        && destinationData.get("zones") instanceof List) {
                  List<Map<String, Object>> zones =
                          (List<Map<String, Object>>) destinationData.get("zones");
                  for (Map<String, Object> zoneData : zones) {
                    processZoneFromDestination(zoneData, destination);
                  }
                }
              }
            } catch (Exception e) {
              log.error("Error al procesar destino: {}", e.getMessage(), e);
            }
          }
        }
      }
    } catch (Exception e) {
      log.error("Error al procesar respuesta de destinos: {}", e.getMessage(), e);
    }

    log.info("Procesados {} destinos", savedCount);
    return savedCount;
  }

  /**
   * Procesa los datos de una zona desde un destino y la guarda en la base de datos.
   *
   * @param zoneData datos de la zona
   * @param destination destino al que pertenece la zona
   */
  private void processZoneFromDestination(Map<String, Object> zoneData, Destination destination) {
    try {
      Integer zoneCode = null;
      if (zoneData.containsKey("zoneCode")) {
        if (zoneData.get("zoneCode") instanceof Integer) {
          zoneCode = (Integer) zoneData.get("zoneCode");
        } else if (zoneData.get("zoneCode") instanceof String) {
          zoneCode = Integer.valueOf((String) zoneData.get("zoneCode"));
        }
      }

      if (zoneCode != null) {

        Zone zone = zoneRepository.findByZoneCodeAndDestination(zoneCode, destination)
                .orElse(new Zone());

        zone.setZoneCode(zoneCode);
        zone.setDestination(destination);

        // Procesar nombre de la zona
        if (zoneData.containsKey("name")) {
          if (zoneData.get("name") instanceof Map) {
            Map<String, Object> nameMap = (Map<String, Object>) zoneData.get("name");
            if (nameMap.containsKey("content")) {
              zone.setName((String) nameMap.get("content"));
            }
          } else if (zoneData.get("name") instanceof String) {
            zone.setName((String) zoneData.get("name"));
          }
        }

        // Si no hay nombre, usar el código como nombre
        if (zone.getName() == null || zone.getName().isEmpty()) {
          zone.setName("Zona " + zoneCode);
        }

        // Guardar la zona
        zoneRepository.save(zone);
      }
    } catch (Exception e) {
      log.error("Error al procesar zona: {}", e.getMessage(), e);
    }
  }

  /**
   * Obtiene un país por su código.
   *
   * @param countryCode código del país
   * @return país
   */
  public Country getCountry(String countryCode) {
    if (countryCode == null) {
      return null;
    }
    return countryRepository.findById(countryCode).orElseThrow(
            () -> new RuntimeException("No se encontró el país con código: " + countryCode)
    );
  }

  /**
   * Obtiene un estado por su código.
   *
   * @param stateCode código del estado
   * @return estado
   */
  public State getState(String stateCode) {
    if (stateCode == null) {
      return null;
    }

    return stateRepository.findByCode(stateCode).orElseThrow(
            () -> new RuntimeException("No se encontró el país con código: " + stateCode)
    );
  }

  /**
   * Obtiene un destino por su código.
   *
   * @param destinationCode código del destino
   * @return destino
   */
  public Destination getDestination(String destinationCode) {
    if (destinationCode == null) {
      return null;
    }
    return destinationRepository.findById(destinationCode).orElseThrow(
            () -> new RuntimeException("No se encontró el país con código: " + destinationCode));
  }

  /**
   * Obtiene una zona por su código.
   *
   * @param zoneCode código de la zona
   * @return zona
   */
  public Zone getZone(Integer zoneCode) {
    if (zoneCode == null) {
      return null;
    }

    return zoneRepository.findByZoneCode(zoneCode).orElseThrow(
            () -> new RuntimeException("No se encontró la zona con código: " + zoneCode));
  }

  /**
   * Guarda un hotel en la base de datos.
   *
   * @param hotelDto datos del hotel
   * @param country país del hotel
   * @param state estado del hotel
   * @param destination destino del hotel
   * @param zone zona del hotel
   * @return hotel guardado
   */
  private Hotel saveHotel(HotelDto hotelDto, Country country,
                          State state, Destination destination, Zone zone) {

    Optional<Hotel> existingHotel = hotelRepository.findById(hotelDto.getCode());

    Hotel hotel = existingHotel.orElse(new Hotel());

    hotel.setCode(hotelDto.getCode());
    hotel.setName(hotelDto.getName().getContent());
    hotel.setDescription(hotelDto.getDescription().getContent());
    hotel.setCountryCode(country != null ? country.getCode() : null);
    hotel.setStateCode(state != null ? state.getCode() : null);
    hotel.setDestinationCode(destination != null ? destination.getCode() : null);
    hotel.setZoneCode(zone != null ? zone.getZoneCode() : null);
    hotel.setLatitude(hotelDto.getCoordinates().getLatitude());
    hotel.setLongitude(hotelDto.getCoordinates().getLongitude());

    // Categoría y cadena
    hotel.setCategoryCode(hotelDto.getCategoryCode());
    hotel.setCategoryGroupCode(hotelDto.getCategoryGroupCode());
    hotel.setChainCode(hotelDto.getChainCode());
    hotel.setAccommodationTypeCode(hotelDto.getAccommodationTypeCode());

    hotel.setAddress(hotelDto.getAddress().getContent());
    hotel.setCity(hotelDto.getCity().getContent());

    hotel.setPostalCode(hotelDto.getPostalCode());
    hotel.setEmail(hotelDto.getEmail());
    hotel.setLicense(hotelDto.getLicense());

    // Guardar campos JSON
    try {
      if (hotelDto.getPhones() != null && !hotelDto.getPhones().isEmpty()) {
        hotel.setPhones(objectMapper.writeValueAsString(hotelDto.getPhones()));
      }

      if (hotelDto.getBoards() != null) {
        hotel.setBoardCodes(objectMapper.writeValueAsString(hotelDto.getBoards()));
      }

      if (hotelDto.getSegments() != null) {
        hotel.setSegmentCodes(objectMapper.writeValueAsString(hotelDto.getSegments()));
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

    } catch (Exception e) {
      log.error("Error serializando campos JSON para hotel {}: {}",
              hotelDto.getCode(), e.getMessage());
    }

    hotel.setWeb(hotelDto.getWeb());
    hotel.setLastUpdate(hotelDto.getLastUpdate());
    hotel.setS2c(hotelDto.getS2C());
    hotel.setRanking(hotelDto.getRanking());

    hotel.setLastUpdated(LocalDateTime.now());

    return hotelRepository.save(hotel);
  }
}