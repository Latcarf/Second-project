package RoyalHouse.model.building;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "real_estate")
public class RealEstate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "real_estate_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "type", nullable = false, length = 25)
    private String type;

    @Column(name = "area_sq_m", nullable = false)
    private Integer areaSqM;

    @Column(name = "price_sq_m", nullable = false, precision = 15, scale = 2)
    private Integer priceSqM;

    @Column(name = "total_price", nullable = false, precision = 15, scale = 2)
    private Integer totalPrice;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "real_estate_photos", joinColumns = @JoinColumn(name = "real_estate_id"))
    @Column(name = "photo_url")
    private List<String> photoUrls;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "details_id")
    private Details details;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne()
    @JoinColumn(name = "new_building_id")
    private NewBuilding newBuilding;

    @PrePersist
    protected void onCreate() {
        this.date = LocalDateTime.now();
    }
}
