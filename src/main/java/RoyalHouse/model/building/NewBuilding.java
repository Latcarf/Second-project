package RoyalHouse.model.building;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "new_building")
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

    @OneToOne
    @JoinColumn(name = "details_id")
    private Details details;

    @OneToOne(mappedBy = "newBuilding", cascade = CascadeType.ALL)
    private NewBuildingInfo newBuildingInfo;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "newBuilding")
    private List<RealEstate> realEstates;
}
