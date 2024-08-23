package RoyalHouse.model.building;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

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

    @OneToOne
    @JoinColumn(name = "details_id")
    private Details details;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "new_building_id")
    private NewBuilding newBuilding;

    @PrePersist
    protected void onCreate() {
        this.date = LocalDateTime.now();
    }
}
