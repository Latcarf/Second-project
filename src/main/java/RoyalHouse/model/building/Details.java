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

    @Column(name = "num_floors")
    private Integer numFloors;

    @Column(name = "num_rooms")
    private Integer numRooms;

    @Column(name = "num_parking_spaces")
    private Integer numParkingSpaces;

    @Column(name = "num_terraces_balconies")
    private Integer numTerracesBalconies;

    @Column(name = "num_commercial_premises")
    private Integer numCommercialPremises;

    @Column(name = "num_recreation_areas")
    private Integer numRecreationAreas;

    @Column(name = "playground")
    private Boolean playground;

    @Column(name = "fitness")
    private Boolean fitness;

    @Column(name = "swimming_pool")
    private Boolean swimmingPool;
}

