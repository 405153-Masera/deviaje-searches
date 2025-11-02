package masera.deviajesearches.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa un regimen de alimentos.
 */
@Entity
@Table(name = "segments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Segment {

  @Id
  private Integer code;

  private String content;
}
