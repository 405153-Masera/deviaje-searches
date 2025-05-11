package masera.deviajesearches.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masera.deviajesearches.dtos.amadeus.response.HotelDto;
import masera.deviajesearches.dtos.hotelbeds.HotelContentResponse;
import masera.deviajesearches.dtos.hotelbeds.HotelDTO;
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
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotelContentServiceImpl implements HotelContentService {

  private final WebClient webClient;
  private final ObjectMapper objectMapper;
  private final HotelRepository hotelRepository;
  private final CountryRepository countryRepository;
  private final StateRepository stateRepository;
  private final DestinationRepository destinationRepository;
  private final ZoneRepository zoneRepository;

  @Value("${hotelbeds.api.key}")
  private String apiKey;

  @Value("${hotelbeds.api.secret}")
  private String apiSecret;

  @Value("${hotelbeds.content.base-url}")
  private String baseUrl;

  @Override
  public List<HotelDto> loadHotels(int from, int to, String language) {
    log.info("Loading hotels from {} to {} in language {}", from, to, language);

    String signature = generateSignature();

    String uri = UriComponentsBuilder.fromPath("/hotel-content-api/1.0/hotels")
            .queryParam("fields", "all")
            .queryParam("language", language)
            .queryParam("from", from)
            .queryParam("to", to)
            .build().toUriString();

    try {
      HotelContentResponse response = webClient.get()
              .uri(uri)
              .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
              .header("Api-key", apiKey)
              .header("X-Signature", signature)
              .retrieve()
              .bodyToMono(HotelContentResponse.class)
              .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                      .filter(throwable -> throwable instanceof WebClientResponseException))
              .block();

      if (response != null && response.getHotels() != null) {
        return saveHotelsAndReturnDTOs(response.getHotels());
      }

      return new ArrayList<>();
    } catch (Exception e) {
      log.error("Error loading hotels: {}", e.getMessage(), e);
      throw new RuntimeException("Error loading hotels", e);
    }
  }

  @Override
  public List<HotelResponseDTO> updateHotels(int from, int to, String language, String lastUpdateTime) {
    log.info("Updating hotels from {} to {} in language {} since {}", from, to, language, lastUpdateTime);

    String signature = generateSignature();

    String uri = UriComponentsBuilder.fromPath("/hotel-content-api/1.0/hotels")
            .queryParam("fields", "all")
            .queryParam("language", language)
            .queryParam("from", from)
            .queryParam("to", to)
            .queryParam("lastUpdateTime", lastUpdateTime)
            .build().toUriString();

    try {
      HotelContentResponse response = webClient.get()
              .uri(uri)
              .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
              .header("Api-key", apiKey)
              .header("X-Signature", signature)
              .retrieve()
              .bodyToMono(HotelContentResponse.class)
              .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                      .filter(throwable -> throwable instanceof WebClientResponseException))
              .block();

      if (response != null && response.getHotels() != null) {
        return saveHotelsAndReturnDTOs(response.getHotels());
      }

      return new ArrayList<>();
    } catch (Exception e) {
      log.error("Error updating hotels: {}", e.getMessage(), e);
      throw new RuntimeException("Error updating hotels", e);
    }
  }

  private List<HotelResponseDTO> saveHotelsAndReturnDTOs(List<HotelDTO> hotelDTOs) {
    List<HotelResponseDTO> result = new ArrayList<>();

    for (HotelDTO hotelDTO : hotelDTOs) {
      try {
        // Procesar país
        Country country = processCountry(hotelDTO);

        // Procesar estado
        State state = processState(hotelDTO, country);

        // Procesar destino
        Destination destination = processDestination(hotelDTO, country);

        // Procesar zona
        Zone zone = processZone(hotelDTO, destination);

        // Guardar hotel
        Hotel hotel = saveHotel(hotelDTO, country, state, destination, zone);

        // Crear DTO para respuesta
        result.add(createHotelResponseDTO(hotel, country));
      } catch (Exception e) {
        log.error("Error processing hotel {}: {}", hotelDTO.getCode(), e.getMessage(), e);
      }
    }

    return result;
  }

  private Country processCountry(HotelDTO hotelDTO) {
    return countryRepository.findById(hotelDTO.getCountryCode())
            .orElseGet(() -> {
              Country country = new Country();
              country.setCode(hotelDTO.getCountryCode());
              // En una implementación real, deberías obtener más datos del país
              return countryRepository.save(country);
            });
  }

  private State processState(HotelDTO hotelDTO, Country country) {
    if (hotelDTO.getStateCode() == null) {
      return null;
    }

    return stateRepository.findByCodeAndCountry(hotelDTO.getStateCode(), country)
            .orElseGet(() -> {
              State state = new State();
              state.setCode(hotelDTO.getStateCode());
              state.setCountry(country);
              // En una implementación real, deberías obtener el nombre del estado
              return stateRepository.save(state);
            });
  }

  private Destination processDestination(HotelDTO hotelDTO, Country country) {
    if (hotelDTO.getDestinationCode() == null) {
      return null;
    }

    return destinationRepository.findById(hotelDTO.getDestinationCode())
            .orElseGet(() -> {
              Destination destination = new Destination();
              destination.setCode(hotelDTO.getDestinationCode());
              destination.setCountry(country);
              // En una implementación real, deberías obtener el nombre del destino
              return destinationRepository.save(destination);
            });
  }

  private Zone processZone(HotelDTO hotelDTO, Destination destination) {
    if (hotelDTO.getZoneCode() == null || destination == null) {
      return null;
    }

    return zoneRepository.findByZoneCodeAndDestination(String.valueOf(hotelDTO.getZoneCode()), destination)
            .orElseGet(() -> {
              Zone zone = new Zone();
              zone.setZoneCode(String.valueOf(hotelDTO.getZoneCode()));
              zone.setDestination(destination);
              // En una implementación real, deberías obtener el nombre de la zona
              return zoneRepository.save(zone);
            });
  }

  private Hotel saveHotel(HotelDTO hotelDTO, Country country, State state, Destination destination, Zone zone) {
    Optional<Hotel> existingHotel = hotelRepository.findById(hotelDTO.getCode());

    Hotel hotel = existingHotel.orElse(new Hotel());

    hotel.setCode(hotelDTO.getCode());
    hotel.setName(hotelDTO.getName());
    hotel.setDescription(hotelDTO.getDescription() != null ? hotelDTO.getDescription().getContent() : null);
    hotel.setCountryCode(country != null ? country.getCode() : null);
    hotel.setStateCode(state != null ? state.getCode() : null);
    hotel.setDestinationCode(destination != null ? destination.getCode() : null);
    hotel.setZoneCode(hotelDTO.getZoneCode());

    if (hotelDTO.getCoordinates() != null) {
      hotel.setLatitude(hotelDTO.getCoordinates().getLatitude());
      hotel.setLongitude(hotelDTO.getCoordinates().getLongitude());
    }

    hotel.setCategoryCode(hotelDTO.getCategoryCode());
    hotel.setCategoryGroupCode(hotelDTO.getCategoryGroupCode());
    hotel.setChainCode(hotelDTO.getChainCode());
    hotel.setAccommodationTypeCode(hotelDTO.getAccommodationTypeCode());

    // Guardar dirección
    if (hotelDTO.getAddress() != null) {
      hotel.setAddress(hotelDTO.getAddress().getContent() != null ?
              hotelDTO.getAddress().getContent().getContent() : null);
      hotel.setCity(hotelDTO.getAddress().getCity());
    }

    hotel.setPostalCode(hotelDTO.getPostalCode());
    hotel.setEmail(hotelDTO.getEmail());
    hotel.setLicense(hotelDTO.getLicense());

    // Guardar datos complejos como JSON
    try {
      if (hotelDTO.getPhones() != null) {
        hotel.setPhones(objectMapper.writeValueAsString(hotelDTO.getPhones()));
      }

      if (hotelDTO.getBoards() != null) {
        hotel.setBoardCodes(objectMapper.writeValueAsString(hotelDTO.getBoards()));
      }

      if (hotelDTO.getSegments() != null) {
        hotel.setSegmentCodes(objectMapper.writeValueAsString(hotelDTO.getSegments()));
      }

      if (hotelDTO.getFacilities() != null) {
        hotel.setFacilities(objectMapper.writeValueAsString(hotelDTO.getFacilities()));
      }

      if (hotelDTO.getTerminals() != null) {
        hotel.setTerminals(objectMapper.writeValueAsString(hotelDTO.getTerminals()));
      }

      if (hotelDTO.getInterestPoints() != null) {
        hotel.setInterestPoints(objectMapper.writeValueAsString(hotelDTO.getInterestPoints()));
      }

      if (hotelDTO.getImages() != null) {
        hotel.setImages(objectMapper.writeValueAsString(hotelDTO.getImages()));
      }

      if (hotelDTO.getRooms() != null) {
        hotel.setRooms(objectMapper.writeValueAsString(hotelDTO.getRooms()));
      }

      if (hotelDTO.getWildcards() != null) {
        hotel.setWildcards(objectMapper.writeValueAsString(hotelDTO.getWildcards()));
      }
    } catch (Exception e) {
      log.error("Error serializing JSON fields for hotel {}: {}", hotelDTO.getCode(), e.getMessage());
    }

    hotel.setWeb(hotelDTO.getWeb());
    hotel.setLastUpdate(hotelDTO.getLastUpdate());
    hotel.setS2c(hotelDTO.getS2C());
    hotel.setRanking(hotelDTO.getRanking());
    hotel.setLastUpdated(LocalDateTime.now());

    return hotelRepository.save(hotel);
  }

  private HotelResponseDTO createHotelResponseDTO(Hotel hotel, Country country) {
    HotelResponseDTO dto = new HotelResponseDTO();
    dto.setCode(hotel.getCode());
    dto.setName(hotel.getName());
    dto.setDescription(hotel.getDescription());

    CountryDTO countryDTO = new CountryDTO();
    countryDTO.setCode(country.getCode());
    countryDTO.setName(country.getDescription());
    dto.setCountry(countryDTO);

    dto.setState(hotel.getStateCode());
    dto.setDestination(hotel.getDestinationCode());
    dto.setZone(hotel.getZoneCode() != null ? hotel.getZoneCode().toString() : null);

    dto.setCategoryCode(hotel.getCategoryCode());
    dto.setCategoryGroupCode(hotel.getCategoryGroupCode());
    dto.setChainCode(hotel.getChainCode());
    dto.setAccommodationTypeCode(hotel.getAccommodationTypeCode());

    dto.setAddress(hotel.getAddress());
    dto.setCity(hotel.getCity());
    dto.setEmail(hotel.getEmail());

    // Extraer URLs de imágenes
    try {
      if (hotel.getImages() != null) {
        List<Map<String, Object>> images = objectMapper.readValue(hotel.getImages(), List.class);
        List<String> imageUrls = images.stream()
                .map(img -> (String) img.get("path"))
                .collect(Collectors.toList());
        dto.setImages(imageUrls);
      }
    } catch (Exception e) {
      log.error("Error deserializing images for hotel {}: {}", hotel.getCode(), e.getMessage());
    }

    return dto;
  }

  private String generateSignature() {
    try {
      long timestamp = System.currentTimeMillis() / 1000;
      String toSign = apiKey + apiSecret + timestamp;

      Mac sha256HMAC = Mac.getInstance("HmacSHA256");
      SecretKeySpec secretKey = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
      sha256HMAC.init(secretKey);

      return Hex.encodeHexString(sha256HMAC.doFinal(toSign.getBytes(StandardCharsets.UTF_8)));
    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
      log.error("Error generating signature: {}", e.getMessage(), e);
      throw new RuntimeException("Error generating API signature", e);
    }
  }
}