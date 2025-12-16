package k23cnt2.nqdproject3.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class NqdCartItemDto {

    private Long productId;
    private String name;
    private String imageUrl;
    private BigDecimal price;
    private int quantity;

    public BigDecimal getSubtotal() {
        if (price == null) return BigDecimal.ZERO;
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
