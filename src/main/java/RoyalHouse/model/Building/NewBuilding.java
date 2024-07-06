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

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "district", nullable = false, length = 100)
    private String district;

    @Column(name = "latitude", nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(name = "year_built", nullable = false)
    private int yearBuilt;

    @Column(name = "num_floors", nullable = false)
    private int numFloors;

    @Column(name = "num_parking_spaces", nullable = false)
    private int numParkingSpaces;

    @Column(name = "playground", nullable = false)
    private boolean playground;

    @Column(name = "fitness", nullable = false)
    private boolean fitness;

    @Column(name = "swimming_pool", nullable = false)
    private boolean swimmingPool;

    @Column(name = "type", nullable = false, length = 25)
    private String type;
}

