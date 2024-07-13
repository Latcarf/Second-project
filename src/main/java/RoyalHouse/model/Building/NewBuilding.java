package RoyalHouse.model.Building;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "new_buildings")
public class NewBuilding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "new_building_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "price", nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @Column(name = "area_sq_m", nullable = false)
    private int areaSqM;

    @Column(name = "type", nullable = false, length = 25)
    private String type;

    @OneToOne(mappedBy = "newBuilding", cascade = CascadeType.ALL)
    private NewBuildingDetails newBuildingDetails;

    @OneToOne(mappedBy = "newBuilding", cascade = CascadeType.ALL)
    private NewBuildingInfo newBuildingInfo;

    @OneToOne(mappedBy = "newBuilding", cascade = CascadeType.ALL)
    private Address address;
}
