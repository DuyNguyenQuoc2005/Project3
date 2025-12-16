package k23cnt2.nqdproject3.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "nqd_order_detail")
@Getter
@Setter
public class NqdOrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ManyToOne -> NqdOrder
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private NqdOrder order;

    // ManyToOne -> NqdProduct
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private NqdProduct product;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "sub_total")
    private BigDecimal subTotal;
}
