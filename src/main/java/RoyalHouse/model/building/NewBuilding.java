package RoyalHouse.model.building;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "new_building")
public class NewBuilding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "new_building_id")
    private Long id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "numApartment")
    private Integer numApartment;

    @Column(name = "sorting_order")
    private Integer sortingOrder;

    @Column(name = "status", length = 50)
    private String status;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "new_building_photos", joinColumns = @JoinColumn(name = "new_building_id"))
    @Column(name = "photo_url")
    private List<String> photoUrls;

    @Column(name = "banner_url")
    private String bannerUrl;

    @Column(name = "panorama_url")
    private String panoramaUrl;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "details_id")
    private Details details;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToOne
    @JoinColumn(name = "information_id")
    private Information information;
}

