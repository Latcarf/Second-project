package RoyalHouse.model.Building;

import RoyalHouse.model.User.User;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "real_estate")
public class RealEstate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "real_estate_id")
    private Long id;

    @Column(name = "type", nullable = false, length = 25)
    private String type;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "price", nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @Column(name = "area_sq_m", nullable = false)
    private int areaSqM;

    @Column(name = "year_built", nullable = false)
    private int yearBuilt;

    @Column(name = "num_floors", nullable = false)
    private int numFloors;

    @Column(name = "num_rooms", nullable = false)
    private int numRooms;

    @Column(name = "num_terraces_balconies", nullable = false)
    private int numTerracesBalconies;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "new_building_id")
    private NewBuilding newBuilding;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;
}
