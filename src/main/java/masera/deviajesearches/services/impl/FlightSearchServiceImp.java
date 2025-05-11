package masera.deviajesearches.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masera.deviajesearches.clients.FlightClient;
import masera.deviajesearches.dtos.amadeus.request.FlightSearchRequest;
import masera.deviajesearches.dtos.amadeus.response.CityDto;
import masera.deviajesearches.services.interfaces.AmadeusTokenService;
import masera.deviajesearches.services.interfaces.FlightSearchService;
import org.springframework.stereotype.Service;

/**
 * Clase de implementación del servicio de búsqueda de vuelos.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FlightSearchServiceImp implements FlightSearchService {

  private final FlightClient flightClient;

  private final AmadeusTokenService amadeusTokenService;

  /**
   * Metodo para buscar vuelos.
   *
   * @param flightSearchRequest el objeto que contiene los parámetros de búsqueda.
   * @return una respuesta de búsqueda de vuelos.
   */
  @Override
  public Object searchFlights(FlightSearchRequest flightSearchRequest) {

    log.info("Iniciando búsqueda de vuelos con parámetros: {}", flightSearchRequest);
    try {
      flightSearchRequest.setMaxResults(60); //Esta línea después debo borrar
      String token = amadeusTokenService.getToken();
      Object flights = flightClient.searchFlightOffers(flightSearchRequest, token).block();
      log.info("Búsqueda de vuelos completada con éxito.");

      return flights;
    } catch (Exception e) {
      log.error("Error de búsqueda de vuelos: {}", e.getMessage());
      throw e;
    }
  }

  @Override
  public Object searchFlightOfferUpsell(Object flightOffer) {
    log.info("Iniciando la búsqueda de ofertas alternativas");
    try {
      String token = amadeusTokenService.getToken();

      Object flightOffersUpselling = this.flightClient
              .searchUpsellingFlightOffers(flightOffer, token).block();

      log.info("Búsqueda de las ofertas alternativas finalizada");
      return flightOffersUpselling;
    } catch (Exception e) {
      log.error("Error al obtener las ofertas alternativas {}", e.getMessage());
      throw e;
    }
  }

  @Override
  public Object verifyFlightOfferPricing(Object flightOffer) {
    log.info("Iniciando la verificación de las ofertas de vuelo");
    try {
      String token = amadeusTokenService.getToken();

      Object response = this.flightClient
                      .verifyFlightOfferPrice(flightOffer, token).block();

      log.info("Verificación de las ofertas finalizadas");
      return response;
    } catch (Exception e) {
      log.error("Error al verificar las ofertas de vuelo {}", e.getMessage());
      throw e;
    }
  }

  @Override
  public List<CityDto> searchCities(String keyword) {
    String token = amadeusTokenService.getToken();
    Object response = flightClient.searchCities(keyword, token).block();

    List<CityDto> citiesDtos = new ArrayList<>();

    if (response instanceof Map) {
      Map<String, Object> responseMap = (Map<String, Object>) response;
      List<Map<String, Object>> data = (List<Map<String, Object>>) responseMap.get("data");

      citiesDtos = data.stream()
              .filter(city -> city.containsKey("iataCode") && city.get("iataCode") != null)
              .map(city -> {
                CityDto cityDto = new CityDto();
                cityDto.setName((String) city.get("name"));
                cityDto.setIataCode((String) city.get("iataCode"));

                if (city.containsKey("address") && city.get("address") instanceof Map) {
                  Map<String, Object> address = (Map<String, Object>) city.get("address");
                  String countryCode = (String) address.get("countryCode");
                  cityDto.setCountry(cityDto.getCountryName(countryCode));
                }

                return cityDto;
              })
              .toList();
    }

    return citiesDtos;

  }
}
