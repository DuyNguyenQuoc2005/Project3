package k23cnt2.nqdproject3.controller;

import jakarta.servlet.http.HttpSession;
import k23cnt2.nqdproject3.model.NqdCartItemDto;
import k23cnt2.nqdproject3.service.NqdCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class NqdCartController {

    private final NqdCartService cartService;

    // ====== Xem giỏ hàng ======
    @GetMapping("/cart")
    public String viewCart(HttpSession session,
                           Model model,
                           @ModelAttribute("cartMessage") String cartMessage) {

        List<NqdCartItemDto> cart = cartService.getCartItems(session);
        BigDecimal total = cartService.getCartTotal(session);
        BigDecimal shippingFee = BigDecimal.ZERO;
        BigDecimal grandTotal = total.add(shippingFee);

        model.addAttribute("cartItems", cart);
        model.addAttribute("cartTotal", total);
        model.addAttribute("shippingFeeDisplay", "Miễn phí");
        model.addAttribute("grandTotal", grandTotal);

        if (cartMessage != null && !cartMessage.isBlank()) {
            model.addAttribute("cartMessage", cartMessage);
        }

        return "cart/cart";
    }

    // ====== Thêm vào giỏ (được gọi bằng fetch trong cart.js) ======
    @PostMapping("/cart/add")
    @ResponseBody
    public String addToCart(@RequestParam("productId") Long productId,
                            @RequestParam(name = "quantity", defaultValue = "1") int quantity,
                            HttpSession session) {

        cartService.addToCart(productId, quantity, session);
        // fetch không cần redirect, trả "OK" là đủ
        return "OK";
    }

    // ====== Tăng / giảm số lượng từ trang giỏ ======
    @PostMapping("/cart/update")
    public String updateQuantity(@RequestParam("productId") Long productId,
                                 @RequestParam("change") int change,
                                 HttpSession session) {

        cartService.changeQuantity(productId, change, session);
        return "redirect:/cart";
    }

    // ====== Xóa 1 sản phẩm ======
    @PostMapping("/cart/remove")
    public String removeItem(@RequestParam("productId") Long productId,
                             HttpSession session) {

        cartService.removeItem(productId, session);
        return "redirect:/cart";
    }

    // ====== Xóa toàn bộ ======
    @PostMapping("/cart/clear")
    public String clearCart(HttpSession session, RedirectAttributes ra) {
        cartService.clearCart(session);
        ra.addFlashAttribute("cartMessage", "Đã xóa toàn bộ giỏ hàng.");
        return "redirect:/cart";
    }
}
