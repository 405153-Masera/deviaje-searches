package masera.deviajesearches.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masera.deviajesearches.clients.HotelClient;
import masera.deviajesearches.dtos.amadeus.request.HotelOffersRequest;
import masera.deviajesearches.dtos.amadeus.request.HotelRequest;
import masera.deviajesearches.exceptions.AmadeusApiException;
import masera.deviajesearches.services.interfaces.AmadeusTokenService;
import masera.deviajesearches.services.interfaces.HotelSearchService;
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
  private final AmadeusTokenService amadeusTokenService;

  @Override
  public Object findHotelsByCity(HotelRequest hotelRequest) {
    log.info("Iniciando búsqueda de hoteles en la ciudad: {}", hotelRequest.getCityCode());

    if (hotelRequest.getCityCode() == null || hotelRequest.getCityCode().isEmpty()) {
      throw new AmadeusApiException("El código de ciudad es obligatorio", 400);
    }

    try {

      String token = amadeusTokenService.getToken();
      Object hotels = hotelClient.findsHotelsByCity(hotelRequest, token).block();
      log.info("Búsqueda de hoteles completada con éxito.");

      return hotels;
    } catch (Exception e) {
      log.error("Error al buscar hoteles: {}", e.getMessage());
      throw e;
    }
  }

  @Override
  public Object findHotelOffers(HotelOffersRequest hotelOffersRequest) {
    log.info("Iniciando búsqueda de ofertas de hoteles: {}", hotelOffersRequest);

    if (hotelOffersRequest.getHotelIds() == null || hotelOffersRequest.getHotelIds().isEmpty()) {
      throw new AmadeusApiException("Debe proporcionar al menos un ID de hotel", 400);
    }

    if (hotelOffersRequest.getCheckInDate() == null) {
      throw new AmadeusApiException("La fecha de entrada es obligatoria", 400);
    }

    if (hotelOffersRequest.getCheckOutDate() == null) {
      throw new AmadeusApiException("La fecha de salida es obligatoria", 400);
    }

    if (hotelOffersRequest.getAdults() == null || hotelOffersRequest.getAdults() < 1) {
      throw new AmadeusApiException("Debe indicar al menos 1 adulto", 400);
    }

    try {

      String token = amadeusTokenService.getToken();
      Object offersHotels = hotelClient.findOffersByHotelsId(hotelOffersRequest, token).block();
      log.info("Búsqueda de ofertas de hoteles completada con éxito.");

      return offersHotels;
    } catch (Exception e) {
      log.error("Error al buscar las ofertas de los hoteles {}", e.getMessage());
      throw e;

    }
  }

  @Override
  public Object getHotelOfferDetails(String offerId) {
    log.info("Iniciando búsqueda de detalles de la oferta de hotel: {}", offerId);

    if (offerId == null || offerId.isEmpty()) {
      throw new AmadeusApiException("El ID de la oferta es obligatorio", 400);
    }

    try {

      String token = amadeusTokenService.getToken();
      Object detailsOffer = hotelClient.getHotelOfferDetails(offerId, token).block();
      log.info("Búsqueda de detalles de la oferta de hotel completada con éxito.");

      return detailsOffer;
    } catch (Exception e) {
      log.error("Error al buscar los detalles de la oferta del hotel {}", e.getMessage());
      throw e;

    }
  }
}
