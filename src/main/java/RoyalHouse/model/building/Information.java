package RoyalHouse.model.building;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "information")
    public class Information {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "information_id")
    private Long id;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "location", columnDefinition = "TEXT", nullable = false)
    private String location;

    @Column(name = "infrastructure", columnDefinition = "TEXT", nullable = false)
    private String infrastructure;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "infrastructure_photos", joinColumns = @JoinColumn(name = "information_id"))
    @Column(name = "infrastructure_photo_url")
    private List<String> infrastructurePhotoUrls;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "specifications", joinColumns = @JoinColumn(name = "information_id"))
    @Column(name = "specification", columnDefinition = "TEXT", nullable = false)
    private List<String> specifications;
}
