package RoyalHouse.model.Building;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "real_estate")
public class    RealEstate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "real_estate_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "type", nullable = false, length = 25)
    private String type;

    @Column(name = "area_sq_m", nullable = false)
    private int areaSqM;

    @Column(name = "price_sq_m", nullable = false, precision = 15, scale = 2)
    private int priceSqM;

    @Column(name = "total_price", nullable = false, precision = 15, scale = 2)
    private int totalPrice;

    @Column(name = "num_rooms", nullable = false)
    private int numRooms;

    @Column(name = "num_floors", nullable = false)
    private int numFloors;

    @Column(name = "num_storeys", nullable = false)
    private int numStoreys;

    @Column(name = "year_built", nullable = false)
    private int yearBuilt;

    @Column(name = "num_terraces_balconies", nullable = false)
    private int numTerracesBalconies;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "new_building_id")
    private NewBuilding newBuilding;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;
}
