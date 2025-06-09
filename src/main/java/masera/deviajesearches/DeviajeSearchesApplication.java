package masera.deviajesearches;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicación principal para el microservicio de búsquedas de Deviaje.
 */
@SpringBootApplication
public class DeviajeSearchesApplication {

  /**
   * Metodo principal que inicia la aplicación.
   *
   * @param args argumentos de línea de comandos
   */
  public static void main(String[] args) {
    SpringApplication.run(DeviajeSearchesApplication.class, args);
  }

}
