package masera.deviajesearches.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa un hotel.
 */
@Entity
@Table(name = "hotels")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hotel {

  @Id
  private String code;

  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  private String countryCode;

  private String stateCode;

  private String destinationCode;

  private Integer zoneCode;

  private Double latitude;

  private Double longitude;

  private String categoryCode;

  private String categoryGroupCode;

  private String chainCode;

  private String accommodationTypeCode;

  private String address;

  private String city;

  private String postalCode;

  private String email;

  private String license;

  private Integer giataCode;

  private LocalDateTime lastUpdated;

  @Lob
  @Column(columnDefinition = "JSON")
  private String boardCodes;

  @Lob
  @Column(columnDefinition = "JSON")
  private String segmentCodes;

  @Lob
  @Column(columnDefinition = "JSON")
  private String phones;

  @Lob
  @Column(columnDefinition = "JSON")
  private String rooms;

  @Lob
  @Column(columnDefinition = "JSON")
  private String facilities;

  @Lob
  @Column(columnDefinition = "JSON")
  private String terminals;

  @Lob
  @Column(columnDefinition = "JSON")
  private String interestPoints;

  @Lob
  @Column(columnDefinition = "JSON")
  private String images;

  @Lob
  @Column(columnDefinition = "JSON")
  private String wildcards;

  @Lob
  @Column(columnDefinition = "JSON")
  private String issues;

  private String web;

  private String lastUpdate;

  private String s2c;

  private Integer ranking;
}
