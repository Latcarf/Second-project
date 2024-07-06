package RoyalHouse.model.Building;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "new_building_details")
public class NewBuildingDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "new_building_id")
    private Long newBuildingId;

    @Column(name = "project_description", columnDefinition = "TEXT")
    private String projectDescription;

    @Column(name = "location", columnDefinition = "TEXT")
    private String location;

    @Column(name = "infrastructure", columnDefinition = "TEXT")
    private String infrastructure;

    @Column(name = "specification", columnDefinition = "TEXT")
    private String specification;

    @OneToOne
    @MapsId
    @JoinColumn(name = "new_building_id")
    private NewBuilding newBuilding;
}
