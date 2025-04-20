package masera.deviajesearches.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ModelMapper y ObjectMapper para la aplicación.
 * Esta clase se encarga de la configuración de ModelMapper y ObjectMapper
 */
@Configuration
public class MappersConfig {

  /**
   * The ModelMapper bean by default.
   *
   * @return the ModelMapper by default.
   */
  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  /**
   * The ModelMapper bean to merge objects.
   *
   * @return the ModelMapper to use in updates.
   */
  @Bean("mergerMapper")
  public ModelMapper mergerMapper() {
    ModelMapper mapper = new ModelMapper();
    mapper.getConfiguration()
            .setPropertyCondition(Conditions.isNotNull());
    return mapper;
  }

  /**
   * The ObjectMapper bean.
   *
   * @return the ObjectMapper with JavaTimeModule included.
   */
  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }

}
