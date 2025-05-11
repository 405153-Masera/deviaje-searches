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

}
