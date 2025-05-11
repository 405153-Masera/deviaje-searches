package masera.deviajesearches.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa un estado.
 */
@Entity
@Table(name = "states")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class State {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String code;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "country_code")
  private Country country;

  private String name;
}
