package masera.deviajesearches.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa un país.
 */
@Entity
@Table(name = "countries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Country {

  @Id
  private String code;

  private String isoCode;

  @Column(columnDefinition = "TEXT")
  private String name;

  @Lob
  @Column(columnDefinition = "JSON")
  private String states;
}