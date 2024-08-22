package RoyalHouse.model.building;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "details")
public class Details {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "details_id")
    private Long id;

    @Column(name = "year_built", nullable = false)
    private Integer yearBuilt;

    @Column(name = "num_floors", nullable = false)
    private Integer numFloors;

    @Column(name = "num_storeys")
    private int numStoreys;

    @Column(name = "num_rooms", nullable = false)
    private Integer numRooms;

    @Column(name = "num_parking_spaces")
    private Integer numParkingSpaces;

    @Column(name = "num_terraces_balconies")
    private Integer numTerracesBalconies;

    @Column(name = "playground")
    private Boolean playground;

    @Column(name = "fitness")
    private Boolean fitness;

    @Column(name = "swimming_pool")
    private Boolean swimmingPool;

    @OneToOne(mappedBy = "details")
    private RealEstate realEstate;

    @OneToOne(mappedBy = "details")
    private NewBuilding newBuilding;
}
