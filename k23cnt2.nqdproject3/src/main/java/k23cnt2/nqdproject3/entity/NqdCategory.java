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
@Table(name = "nqd_category")
public class NqdCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;        // Ví dụ: Bánh nướng, Bánh dẻo, Combo hộp quà

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "category")
    @ToString.Exclude
    private List<NqdProduct> products;
}
