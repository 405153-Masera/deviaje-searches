package masera.deviajesearches.configs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringDocConfigTest {

  @Autowired
  private WebTestClient webTestClient;

  @Test
  void getDocumentation() throws IOException {

    String responseBody = this.webTestClient.get()
            .uri("/v3/api-docs")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .returnResult()
            .getResponseBody();

    assertNotNull(responseBody, "El cuerpo de la respuesta no debe ser nulo");
    assertTrue(responseBody.contains("openapi"),
            "La respuesta debe contener la especificación OpenAPI");

    Path specs = Paths.get("docs/api_doc");
    Files.createDirectories(specs);
    Files.writeString(specs.resolve("swagger.json"), responseBody);
  }

  @Test
  void swaggerUiIsAccessible() {
    webTestClient
            .get()
            .uri("/swagger-ui/index.html")
            .exchange()
            .expectStatus().isOk();
  }
}