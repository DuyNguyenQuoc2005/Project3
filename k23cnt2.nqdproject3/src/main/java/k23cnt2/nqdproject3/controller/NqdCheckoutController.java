package k23cnt2.nqdproject3.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import k23cnt2.nqdproject3.entity.NqdOrder;
import k23cnt2.nqdproject3.entity.NqdOrderDetail;
import k23cnt2.nqdproject3.entity.NqdProduct;
import k23cnt2.nqdproject3.entity.NqdUser;
import k23cnt2.nqdproject3.model.NqdCartItemDto;
import k23cnt2.nqdproject3.model.NqdOrderRequestDto;
import k23cnt2.nqdproject3.repository.NqdOrderRepository;
import k23cnt2.nqdproject3.repository.NqdProductRepository;
import k23cnt2.nqdproject3.service.NqdCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static k23cnt2.nqdproject3.controller.NqdAuthController.SESSION_USER_KEY;

@Controller
@RequiredArgsConstructor
public class NqdCheckoutController {

    private final NqdCartService cartService;
    private final NqdOrderRepository orderRepository;
    private final NqdProductRepository productRepository;

    // ============ GET: /checkout ============
    @GetMapping("/checkout")
    public String showCheckoutForm(Model model, HttpSession session) {

        List<NqdCartItemDto> cartItems = cartService.getCartItems(session);
        BigDecimal cartTotal = cartService.getCartTotal(session);
        BigDecimal shippingFee = BigDecimal.ZERO;
        BigDecimal grandTotal = cartTotal.add(shippingFee);

        // nếu chưa có orderForm trong model thì tạo mới
        if (!model.containsAttribute("orderForm")) {
            NqdOrderRequestDto form = new NqdOrderRequestDto();
            form.setTotalAmount(grandTotal);
            model.addAttribute("orderForm", form);
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartTotal", cartTotal);
        model.addAttribute("shippingFeeDisplay", "Miễn phí");
        model.addAttribute("grandTotal", grandTotal);

        return "order/checkout";
    }

    // ============ POST: /checkout ============
    @PostMapping("/checkout")
    public String placeOrder(@Valid @ModelAttribute("orderForm") NqdOrderRequestDto orderForm,
                             BindingResult bindingResult,
                             Model model,
                             HttpSession session) {

        List<NqdCartItemDto> cartItems = cartService.getCartItems(session);

        // Giỏ trống thì báo lỗi
        if (cartItems == null || cartItems.isEmpty()) {
            bindingResult.reject("emptyCart", "Giỏ hàng đang trống, không thể đặt hàng.");
        }

        // Nếu form lỗi → nạp lại dữ liệu và quay về trang checkout
        if (bindingResult.hasErrors()) {
            BigDecimal cartTotal = cartService.getCartTotal(session);
            BigDecimal shippingFee = BigDecimal.ZERO;
            BigDecimal grandTotal = cartTotal.add(shippingFee);

            model.addAttribute("cartItems", cartItems);
            model.addAttribute("cartTotal", cartTotal);
            model.addAttribute("shippingFeeDisplay", "Miễn phí");
            model.addAttribute("grandTotal", grandTotal);

            return "order/checkout";
        }

        // Tính lại tổng cho chắc
        BigDecimal cartTotal = cartService.getCartTotal(session);
        BigDecimal shippingFee = BigDecimal.ZERO;
        BigDecimal grandTotal = cartTotal.add(shippingFee);
        orderForm.setTotalAmount(grandTotal);

        // 1. Tạo order
        NqdOrder order = new NqdOrder();

        // Lấy user đang login (nếu có)
        NqdUser currentUser = (NqdUser) session.getAttribute(SESSION_USER_KEY);
        order.setCustomer(currentUser);          // có thể là null nếu khách chưa login

        order.setFullName(orderForm.getFullName());
        order.setPhone(orderForm.getPhone());
        order.setShipAddress(orderForm.getAddress());
        order.setNote(orderForm.getNote());
        order.setPaymentMethod(orderForm.getPaymentMethod());
        order.setTotalAmount(orderForm.getTotalAmount());
        order.setStatus("PENDING");
        order.setOrderDate(LocalDateTime.now());
        order.setRequiredDate(null);

        // 2. Tạo list chi tiết đơn hàng từ giỏ
        List<NqdOrderDetail> details = new ArrayList<>();
        for (NqdCartItemDto item : cartItems) {
            NqdProduct p = productRepository.findById(item.getProductId())
                    .orElseThrow(() ->
                            new RuntimeException("Không tìm thấy sản phẩm: " + item.getProductId()));

            NqdOrderDetail d = new NqdOrderDetail();
            d.setOrder(order);
            d.setProduct(p);
            d.setQuantity(item.getQuantity());
            d.setUnitPrice(item.getPrice());
            d.setSubTotal(item.getSubtotal());

            details.add(d);
        }
        order.setDetails(details);

        // 3. Lưu DB
        orderRepository.save(order);

        // 4. Clear giỏ
        cartService.clearCart(session);

        // 5. Redirect sang trang cảm ơn
        return "redirect:/checkout/success";
    }

    // ============ GET: /checkout/success ============
    @GetMapping("/checkout/success")
    public String checkoutSuccess() {
        // đơn giản render template order/checkout-success.html
        return "order/checkout-success";
    }
}
