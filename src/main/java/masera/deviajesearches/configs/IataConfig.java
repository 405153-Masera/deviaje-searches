package masera.deviajesearches.configs;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración para la API de API Ninjas.
 */
@Configuration
@Getter
public class IataConfig {

  @Value("${iata.api.base-url}")
  private String baseUrl;

  @Value("${iata.api.key}")
  private String apiKey;
}