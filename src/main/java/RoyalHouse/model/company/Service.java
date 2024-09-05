package RoyalHouse.model.company;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "services")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Long id;

    @Column(name = "heading", nullable = false, length = 100)
    private String heading;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "banner_url")
    private String bannerUrl;


}
