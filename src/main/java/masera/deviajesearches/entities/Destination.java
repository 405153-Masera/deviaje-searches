package masera.deviajesearches.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa un destino.
 */
@Entity
@Table(name = "destinations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Destination {

  @Id
  private String code;

  @Column(nullable = false)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "country_code")
  private Country country;

  @OneToMany(mappedBy = "destination", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Zone> zones;
}