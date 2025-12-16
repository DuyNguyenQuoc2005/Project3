package k23cnt2.nqdproject3.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class NqdOrderRequestDto {

    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String phone;

    @NotBlank(message = "Địa chỉ giao hàng không được để trống")
    private String address;

    private String note;

    @NotBlank(message = "Vui lòng chọn phương thức thanh toán")
    private String paymentMethod;

    // Tổng tiền sẽ set từ giỏ hàng, không nhập từ form
    private BigDecimal totalAmount;
}
