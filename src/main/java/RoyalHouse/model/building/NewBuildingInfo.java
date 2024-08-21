package RoyalHouse.model.building;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "new_building_info")
public class NewBuildingInfo {

    @Id
    @Column(name = "new_building_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "new_building_id")
    private NewBuilding newBuilding;

    @Column(name = "project_description", columnDefinition = "TEXT")
    private String projectDescription;

    @Column(name = "location", columnDefinition = "TEXT")
    private String location;

    @Column(name = "infrastructure", columnDefinition = "TEXT")
    private String infrastructure;

    @Column(name = "specification", columnDefinition = "TEXT")
    private String specification;
}
