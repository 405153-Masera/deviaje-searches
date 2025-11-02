package masera.deviajesearches.exceptions;

import lombok.Getter;

/**
 * Excepción personalizada para errores relacionados con la API de HotelBeds.
 */
@Getter
public class HotelBedsApiException extends RuntimeException {

  private final int statusCode;
  private String internalCode;

  /**
   * Constructor de la clase.
   *
   * @param message Mensaje de error.
   * @param statusCode Código de estado HTTP.
   * @param internalCode Código interno de hotelbeds
   */
  public HotelBedsApiException(String message, int statusCode,  String internalCode) {
    super(message);
    this.statusCode = statusCode;
    this.internalCode = internalCode;
  }

  /**
   * Constructor de la clase.
   *
   * @param message Mensaje de error.
   * @param statusCode Código de estado HTTP.
   */
  public HotelBedsApiException(String message, int statusCode) {
    super(message);
    this.statusCode = statusCode;
  }

  /**
   * Constructor de la clase.
   *
   * @param message Mensaje de error.
   * @param cause Causa de la excepción.
   * @param statusCode Código de estado HTTP.
   */
  public HotelBedsApiException(String message, Throwable cause, int statusCode) {
    super(message, cause);
    this.statusCode = statusCode;
  }
}
