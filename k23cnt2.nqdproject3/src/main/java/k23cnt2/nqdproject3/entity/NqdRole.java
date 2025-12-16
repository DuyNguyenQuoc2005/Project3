package k23cnt2.nqdproject3.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "nqd_role")
public class NqdRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;    // ROLE_USER, ROLE_ADMIN

    @Column(length = 255)
    private String description;

    @OneToMany(mappedBy = "role")
    @ToString.Exclude
    private List<NqdUser> users;
}
