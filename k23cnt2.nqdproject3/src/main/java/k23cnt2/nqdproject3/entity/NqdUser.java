package k23cnt2.nqdproject3.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "nqd_user")
public class NqdUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(length = 150)
    private String fullName;

    @Column(length = 150)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 255)
    private String address;

    private Boolean active = true;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private NqdRole role;       // USER hoặc ADMIN

    @OneToMany(mappedBy = "customer")
    @ToString.Exclude
    private List<NqdOrder> orders;
}
