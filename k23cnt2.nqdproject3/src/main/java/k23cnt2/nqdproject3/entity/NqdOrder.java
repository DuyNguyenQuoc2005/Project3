package k23cnt2.nqdproject3.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "nqd_order")
public class NqdOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // KHÁCH HÀNG ĐẶT ĐƠN
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")     // trùng với cột trong bảng
    private NqdUser customer;

    @Column(name = "full_name", length = 150)
    private String fullName;

    @Column(length = 20)
    private String phone;

    @Column(name = "ship_address", length = 255)
    private String shipAddress;

    @Column(name = "total_amount", precision = 14, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Column(length = 30)
    private String status;        // PENDING, ...

    @Column(length = 500)
    private String note;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "required_date")
    private LocalDateTime requiredDate;

    // Danh sách chi tiết đơn
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NqdOrderDetail> details;
}
