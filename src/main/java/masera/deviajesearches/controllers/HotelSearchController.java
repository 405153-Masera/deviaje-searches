package masera.deviajesearches.controllers;

import lombok.RequiredArgsConstructor;
import masera.deviajesearches.dtos.amadeus.request.HotelSearchRequest;
import masera.deviajesearches.dtos.amadeus.request.HotelRequest;
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
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/searches/hotels")
public class HotelSearchController {

  private final HotelSearchService hotelSearchService;

  /**
   * Controlador para buscar hoteles por ciudad.
   *
   * @param hotelRequest parámetros de búsqueda de hoteles.
   * @return una respuesta con los resultados de la búsqueda.
   */
  @PostMapping("/by-city")
  public ResponseEntity<Object> findHotelsByCity(@RequestBody HotelRequest hotelRequest) {
    Object result = hotelSearchService.findHotelsByCity(hotelRequest);
    return ResponseEntity.ok(result);
  }

  /**
   * Controlador para buscar ofertas de hoteles.
   *
   * @param hotelSearchRequest parámetros de búsqueda de ofertas de hoteles.
   * @return una respuesta con los resultados de la búsqueda.
   */
  @PostMapping("/offers")
  public ResponseEntity<Object> findHotelOffers(
          @RequestBody HotelSearchRequest hotelSearchRequest) {
    Object result = hotelSearchService.findHotelOffers(hotelSearchRequest);
    return ResponseEntity.ok(result);
  }

  /**
   * Controlador para obtener detalles de una oferta de hotel.
   *
   * @param offerId identificador de la oferta de hotel.
   * @return una respuesta con los detalles de la oferta.
   */
  @GetMapping("/offers/{offerId}")
  public ResponseEntity<Object> getHotelOfferDetails(@PathVariable String offerId) {
    Object result = hotelSearchService.getHotelOfferDetails(offerId);
    return ResponseEntity.ok(result);
  }
}
