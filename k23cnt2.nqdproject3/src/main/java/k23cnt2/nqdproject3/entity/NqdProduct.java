package k23cnt2.nqdproject3.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "nqd_product")
public class NqdProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private NqdCategory category;

    @Column(nullable = false, length = 200)
    private String name;                // Tên bánh

    @Column(length = 200, unique = true)
    private String slug;                // Đường dẫn SEO (tùy dùng)

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;          // Giá bán hiện tại

    @Column(precision = 12, scale = 2)
    private BigDecimal originalPrice;  // Giá gốc (nếu có khuyến mãi)

    private Integer weight;            // Trọng lượng (gram)

    private Integer eggCount;          // Số trứng muối

    @Column(length = 100)
    private String flavor;             // Hương vị: thập cẩm, đậu xanh, trà xanh...

    @Column(length = 255)
    private String imageUrl;

    @Column(length = 500)
    private String shortDescription;

    @Lob
    private String description;        // Mô tả chi tiết

    private Boolean newProduct = false;
    private Boolean bestSeller = false;
    private Boolean active = true;

    // Nếu muốn quản lý tồn kho nhanh
    private Integer stockQuantity;
}
