package masera.deviajesearches.controllers;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import masera.deviajesearches.dtos.amadeus.ErrorApi;
import masera.deviajesearches.exceptions.AmadeusApiException;
import masera.deviajesearches.exceptions.HotelBedsApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.server.ResponseStatusException;

/**
 * Clase para el manejo global de excepciones.
 */
@RestControllerAdvice
@Slf4j
public class ControllerException {

  /**
   * Manejador para errores de la API de Amadeus.
   *
   * @param e excepción de la API de Amadeus.
   * @return ResponseEntity con el error.
   */
  @ExceptionHandler(AmadeusApiException.class)
  public ResponseEntity<ErrorApi> handleAmadeusApiException(AmadeusApiException e) {
    log.error("Error en Amadeus API: {} - Status: {}", e.getMessage(), e.getStatusCode());
    HttpStatus status = HttpStatus.valueOf(e.getStatusCode());
    ErrorApi error = buildError(e.getMessage(), status);
    error.setSource("AMADEUS");
    return ResponseEntity.status(status).body(error);
  }

  /**
   * Manejador para errores de la API de Hotelbeds.
   *
   * @param e excepción de la API de Hotelbeds.
   * @return ResponseEntity con el error.
   */
  @ExceptionHandler(HotelBedsApiException.class)
  public ResponseEntity<ErrorApi> handleHotelBedsApiException(HotelBedsApiException e) {
    log.error("Error en HotelBeds API: {} - Status: {}", e.getMessage(), e.getStatusCode());
    HttpStatus status = HttpStatus.valueOf(e.getStatusCode());
    ErrorApi error = buildError(e.getMessage(), status);
    error.setSource("HOTELBEDS");
    return ResponseEntity.status(status).body(error);
  }

  /**
   * Maneja errores de timeout y conexión con API externas.
   *
   * @param ex      Excepción de petición HTTP
   * @return ResponseEntity con el error formateado
   */
  @ExceptionHandler(WebClientRequestException.class)
  public ResponseEntity<ErrorApi> handleWebClientRequestException(WebClientRequestException ex) {
    log.error("Error de conexión con API externa: {}", ex.getMessage());

    ErrorApi error = ErrorApi.builder()
            .timestamp(LocalDateTime.now().toString())
            .status(HttpStatus.SERVICE_UNAVAILABLE.value())
            .error("Service Unavailable")
            .message("No se pudo conectar con el servicio externo")
            .source("EXTERNAL_API")
            .build();

    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
  }

  /**
   * Manejador para errores de validación (400).
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationExceptions(
          MethodArgumentNotValidException e) {
    Map<String, String> fieldErrors = new HashMap<>();
    e.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      fieldErrors.put(fieldName, errorMessage);
    });

    Map<String, Object> response = new HashMap<>();
    response.put("timestamp", String.valueOf(Timestamp.from(ZonedDateTime.now().toInstant())));
    response.put("status", HttpStatus.BAD_REQUEST.value());
    response.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
    response.put("source", "BACKEND");
    response.put("validationErrors", fieldErrors);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  /**
   * Manejador para excepciones HTTP específicas.
   */
  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ErrorApi> handleError(ResponseStatusException e) {
    ErrorApi error = buildError(e.getReason(), HttpStatus.valueOf(e.getStatusCode().value()));
    return ResponseEntity.status(e.getStatusCode()).body(error);
  }

  /**
   * Manejador global para cualquier otra excepción (500).
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorApi> handleError(Exception e) {
    ErrorApi error = buildError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    error.setSource("BACKEND");
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }

  /**
   * Metodo para construir un objeto error API.
   *
   * @param message mensaje de error a arrojar.
   * @param status  código de HTTP.
   * @return un ErrorApi.
   */
  private ErrorApi buildError(String message, HttpStatus status) {
    return ErrorApi.builder()
            .timestamp(String.valueOf(Timestamp.from(ZonedDateTime.now().toInstant())))
            .error(status.getReasonPhrase())
            .status(status.value())
            .message(message)
            .build();
  }
}
