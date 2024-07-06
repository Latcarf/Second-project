package RoyalHouse.model.Building;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "real_estate_banner")
public class RealEstateBanner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "real_estate_banner_id")
    private Long id;

    @Column(name = "text", nullable = false, length = 100)
    private String text;

    @Column(name = "link", nullable = false, length = 200)
    private String link;

    @ManyToOne
    @JoinColumn(name = "real_estate_id", nullable = false)
    private RealEstate realEstate;
}
