package masera.deviajesearches.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa una terminal de transporte (aeropuerto, estación de bus, etc.).
 */
@Entity
@Table(name = "terminals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Terminal {

  @Id
  @Column
  private String code;

  @Column
  private String type;

  @Column
  private String country;

  @Column
  private String name;

  @Column
  private String description;

  @Column
  private String languageCode;
}
