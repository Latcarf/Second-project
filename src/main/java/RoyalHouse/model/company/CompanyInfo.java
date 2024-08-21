package RoyalHouse.model.company;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "company_info")
public class CompanyInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Long id;

    @Column(name = "heading", nullable = false, length = 100)
    private String heading;

    @Column(name = "banner_text", nullable = false, length = 100)
    private String bannerText;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
