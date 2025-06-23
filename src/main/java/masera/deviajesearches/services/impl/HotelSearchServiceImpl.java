package masera.deviajesearches.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masera.deviajesearches.clients.HotelClient;
import masera.deviajesearches.dtos.amadeus.request.HotelSearchRequest;
import masera.deviajesearches.dtos.amadeus.response.HotelResponseDto;
import masera.deviajesearches.dtos.amadeus.response.HotelSearchResponse;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.ImageDto;
import masera.deviajesearches.entities.Hotel;
import masera.deviajesearches.repositories.HotelRepository;
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
  private final ModelMapper modelMapper;
  private final ObjectMapper objectMapper;

  @Override
  public HotelSearchResponse searchHotels(HotelSearchRequest request) {
    log.info("Buscando hoteles con los siguientes parámetros: {}", request);

    try {
      return hotelClient.searchHotels(request).block();
    } catch (Exception e) {
      log.error("Error al buscar hoteles: {}", e.getMessage(), e);
      throw new RuntimeException("Error al buscar hoteles", e);
    }
  }

  @Override
  public HotelResponseDto getHotelDetails(String hotelCode) {
    log.info("Obteniendo detalles del hotel con código: {}", hotelCode);

    try {
      // Buscar el hotel en la base de datos
      Optional<Hotel> optionalHotel = hotelRepository.findByCode(hotelCode);

      if (optionalHotel.isPresent()) {
        Hotel hotel = optionalHotel.get();

        // Mapear la entidad a DTO
        HotelResponseDto responseDto = modelMapper.map(hotel, HotelResponseDto.class);

        // Procesar campos JSON
        processJsonFields(hotel, responseDto);

        return responseDto;
      } else {
        throw new RuntimeException("Hotel no encontrado con código: " + hotelCode);
      }
    } catch (Exception e) {
      log.error("Error al obtener detalles del hotel: {}", e.getMessage(), e);
      throw new RuntimeException("Error al obtener detalles del hotel", e);
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
      // Procesar imágenes
      if (hotel.getImages() != null && !hotel.getImages().isEmpty()) {
        List<ImageDto> images = objectMapper.readValue(hotel.getImages(),
                new TypeReference<>() {});
        responseDto.setImages(images);
      } else {
        responseDto.setImages(new ArrayList<>());
      }
    } catch (JsonProcessingException e) {
      log.error("Error al procesar campos JSON del hotel: {}", e.getMessage());
    }
  }
}
