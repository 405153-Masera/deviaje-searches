package masera.deviajesearches.dtos.amadeus.response.hotelbeds;

import lombok.Data;

/**
 * DTO que representa los datos de auditoría de una respuesta de Hotelbeds.
 */
@Data
public class AuditData {
  private String processTime;
  private String timestamp;
  private String requestHost;
  private String serverId;
  private String environment;
  private String release;
  private String token;
  private String internal;
}
