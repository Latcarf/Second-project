package RoyalHouse.model.building;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "new_building_details")
public class NewBuildingDetails {

    @Id
    @Column(name = "new_building_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "new_building_id")
    private NewBuilding newBuilding;

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
}
