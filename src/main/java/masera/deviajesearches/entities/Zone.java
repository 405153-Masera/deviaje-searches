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
 * Entidad que representa una zona.
 */
@Entity
@Table(name = "zones")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Zone {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private Integer zoneCode;

  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "destination_code")
  private Destination destination;
}
