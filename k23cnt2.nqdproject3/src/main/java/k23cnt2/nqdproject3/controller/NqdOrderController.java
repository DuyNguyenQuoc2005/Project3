package k23cnt2.nqdproject3.controller;

import jakarta.servlet.http.HttpSession;
import k23cnt2.nqdproject3.entity.NqdOrder;
import k23cnt2.nqdproject3.entity.NqdUser;
import k23cnt2.nqdproject3.repository.NqdOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NqdOrderController {

    private final NqdOrderRepository orderRepository;

    @GetMapping("/order")
    public String listMyOrders(HttpSession session, Model model) {
        NqdUser currentUser = (NqdUser) session.getAttribute(NqdAuthController.SESSION_USER_KEY);

        if (currentUser == null) {
            model.addAttribute("needLogin", true);
            return "order/list-order";
        }

        List<NqdOrder> orders = orderRepository.findByCustomerOrderByOrderDateDesc(currentUser);
        model.addAttribute("orders", orders);
        model.addAttribute("needLogin", false);

        return "order/list-order";
    }



    // Chi tiết 1 đơn
    @GetMapping("/order/{id}")
    public String viewOrderDetail(@PathVariable("id") Long id, Model model) {
        NqdOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng: " + id));

        model.addAttribute("order", order);
        model.addAttribute("details", order.getDetails());

        // trước: return "order/detail";
        return "order/detail-order";
    }
}
