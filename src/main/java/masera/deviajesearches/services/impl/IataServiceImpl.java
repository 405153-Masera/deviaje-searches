package masera.deviajesearches.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masera.deviajesearches.clients.IataClient;
import masera.deviajesearches.dtos.amadeus.response.IataResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Servicio para manejar la lógica de búsqueda de aeropuertos por código IATA.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class IataServiceImpl {

  private final IataClient iataClient;

  /**
   * Obtiene la información de un aeropuerto por su código IATA.
   *
   * @param iataCode código IATA del aeropuerto.
   * @return Mono con la respuesta del aeropuerto.
   */
  public Mono<IataResponse> getAirportByIata(String iataCode) {
    log.info("Procesando solicitud de aeropuerto con código IATA: {}", iataCode);
    return iataClient.getAirportByIata(iataCode)
            .switchIfEmpty(Mono.error(new RuntimeException(
                    "No se encontró el aeropuerto con el código IATA: " + iataCode)));
  }
}