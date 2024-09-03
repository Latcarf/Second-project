package RoyalHouse.model.company;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "company_info")
public class CompanyInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long id;

    @Column(name = "heading", nullable = false, length = 100)
    private String heading;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "banner_url")
    private String bannerUrl;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "company_info_photos", joinColumns = @JoinColumn(name = "company_id"))
    @Column(name = "photo_url")
    private List<String> photoUrls;
}
