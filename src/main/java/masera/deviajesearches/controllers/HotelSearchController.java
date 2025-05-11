package masera.deviajesearches.controllers;

import lombok.RequiredArgsConstructor;
import masera.deviajesearches.services.interfaces.HotelSearchService;
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
