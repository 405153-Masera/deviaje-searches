package masera.deviajesearches.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masera.deviajesearches.dtos.amadeus.request.HotelSearchRequest;
import masera.deviajesearches.dtos.amadeus.response.HotelResponseDto;
import masera.deviajesearches.dtos.amadeus.response.HotelSearchResponse;
import masera.deviajesearches.services.interfaces.HotelSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para la búsqueda de hoteles.
 * Este controlador maneja las solicitudes relacionadas con la búsqueda de hoteles.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/searches/hotels")
public class HotelSearchController {

  private final HotelSearchService hotelSearchService;

  /**
   * Busca hoteles según los criterios especificados.
   *
   * @param request solicitud de búsqueda de hoteles
   * @return respuesta con los resultados de la búsqueda
   */
  @PostMapping
  public ResponseEntity<HotelSearchResponse> searchHotels(
          @Valid @RequestBody HotelSearchRequest request) {
    HotelSearchResponse response = hotelSearchService.searchHotels(request);
    return ResponseEntity.ok(response);
  }

  /**
   * Obtiene detalles de un hotel específico.
   *
   * @param hotelCode código del hotel
   * @return detalles del hotel
   */
  @GetMapping("/{hotelCode}")
  public ResponseEntity<HotelResponseDto> getHotelDetails(@PathVariable String hotelCode) {
    HotelResponseDto hotel = hotelSearchService.getHotelDetails(hotelCode);
    return ResponseEntity.ok(hotel);
  }
}
