package masera.deviajesearches.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masera.deviajesearches.exceptions.AmadeusApiException;
import masera.deviajesearches.exceptions.HotelBedsApiException;
import masera.deviajesearches.utils.dtos.AmadeusError;
import masera.deviajesearches.utils.dtos.AmadeusErrorResponse;
import masera.deviajesearches.utils.dtos.HotelBedsErrorResponse;
import masera.deviajesearches.utils.dtos.HotelBedsErrorV2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Clase para manejar errores de Amadeus.
 * Esta clase se encarga de manejar los errores que pueden
 * ocurrir al interactuar con la API de Amadeus.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ErrorHandler {

  private final ObjectMapper objectMapper;

  /**
   * Procesa una excepción WebClientResponseException de Amadeus
   * y la convierte en una AmadeusApiException con los detalles adecuados.
   *
   * @param e La excepción original
   * @return Una nueva AmadeusApiException con los detalles de error de Amadeus
   */
  public AmadeusApiException handleAmadeusError(WebClientResponseException e) {
    try {
      AmadeusErrorResponse errorBody = objectMapper.readValue(
              e.getResponseBodyAsString(), AmadeusErrorResponse.class);

      if (errorBody.getErrors() != null) {
        List<AmadeusError> errors = errorBody.getErrors();

        if (!errors.isEmpty()) {
          AmadeusError amadeusError = errors.getFirst();

          int status = amadeusError.getStatus() != null
                  ? amadeusError.getStatus() : e.getStatusCode().value();
          String message = getString(amadeusError);

          return new AmadeusApiException(message, status);
        }
      }
    } catch (JsonProcessingException ex) {
      log.error("Error al parsear respuesta de error de Amadeus", ex);
    }
    
    return new AmadeusApiException(
            "Error al comunicarse con Amadeus: " + e.getStatusText(),
            e.getStatusCode().value());
  }

  /**
   * Procesa una excepción WebClientResponseException de HotelBeds
   * y la convierte en una HotelBedsApiException con los detalles adecuados.
   *
   * @param e La excepción original de WebClient
   * @return Una nueva HotelBedsApiException con los detalles formateados
   */
  public HotelBedsApiException handleHotelBedsError(WebClientResponseException e) {
    try {

      if (e.getStatusCode() == HttpStatus.BAD_REQUEST
              || e.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {

        HotelBedsErrorResponse errorResponse = objectMapper.readValue(
                e.getResponseBodyAsString(), HotelBedsErrorResponse.class);

        String message = errorResponse.getError().getCode() + " - "
                + errorResponse.getError().getMessage();
        return new HotelBedsApiException(message, e.getStatusCode().value());

      } else {
        HotelBedsErrorV2 errorResponse = objectMapper.readValue(
                e.getResponseBodyAsString(), HotelBedsErrorV2.class);
        String message = errorResponse.getError();
        return new HotelBedsApiException(message, e.getStatusCode().value());
      }

    } catch (JsonProcessingException ex) {
      log.error("Error inesperado al procesar error de HotelBeds", ex);
    }

    return new HotelBedsApiException(
            "Error al comunicarse con HotelBeds Content API: " + e.getStatusText(),
            e.getStatusCode().value());
  }

  private static String getString(AmadeusError amadeusError) {
    String detail = amadeusError.getDetail() != null
            ? amadeusError.getDetail() : "Error desconocido";
    int code = amadeusError.getCode() != null
            ? amadeusError.getCode() : 0;
    String title = amadeusError.getTitle() != null
            ? amadeusError.getTitle() : "";

    String message = detail;
    if (!title.isEmpty()) {
      message = title + ": " + message;
    }
    if (code != 0) {
      message = "Error " + code + " - " + message;
    }
    return message;
  }
}
