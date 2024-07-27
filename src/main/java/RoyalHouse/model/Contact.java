package RoyalHouse.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
    @Table(name = "contact")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id")
    private Long id;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "phone", nullable = false, length = 17)
    private String phone;

    @Column(name = "telegram", nullable = false, length = 50)
    private String telegram;

    @Column(name = "viber", nullable = false, length = 50)
    private String viber;

    @Column(name = "instagram", nullable = false, length = 50)
    private String instagram;

    @Column(name = "facebook", nullable = false, length = 50)
    private String facebook;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "password", nullable = false, length = 255)
    private String password;
}
