package k23cnt2.nqdproject3.controller;

import jakarta.servlet.http.HttpSession;
import k23cnt2.nqdproject3.entity.NqdOrder;
import k23cnt2.nqdproject3.entity.NqdUser;
import k23cnt2.nqdproject3.repository.NqdOrderRepository;
import k23cnt2.nqdproject3.repository.NqdUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static k23cnt2.nqdproject3.controller.NqdAuthController.SESSION_USER_KEY;

@Controller
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class NqdAdminOrderController {

    private final NqdOrderRepository orderRepository;
    private final NqdUserRepository userRepository;

    // check admin giống mấy controller kia
    private boolean isNotAdmin(HttpSession session) {
        Object obj = session.getAttribute(SESSION_USER_KEY);
        if (!(obj instanceof NqdUser user)) {
            return true;
        }
        return user.getRole() == null
                || user.getRole().getName() == null
                || !user.getRole().getName().equalsIgnoreCase("ROLE_ADMIN");
    }

    // các trạng thái đơn hàng cho form
    private List<String> getOrderStatuses() {
        return Arrays.asList("PENDING", "PROCESSING", "SHIPPING", "COMPLETED", "CANCELED");
    }

    // phương thức thanh toán
    private List<String> getPaymentMethods() {
        return Arrays.asList("COD", "MOMO", "VNPAY");
    }
    // ========== LIST ĐƠN HÀNG ==========
    @GetMapping({"", "/"})
    public String listOrders(Model model, HttpSession session) {
        if (isNotAdmin(session)) {
            return "redirect:/";
        }

        List<NqdOrder> orders = orderRepository.findAll(); // có thể sort nếu thích
        model.addAttribute("orders", orders);
        model.addAttribute("pageTitle", "Quản lý đơn hàng");

        return "admin/order-list";
    }

    // ========== FORM THÊM ==========
    @GetMapping("/new")
    public String showCreateForm(Model model, HttpSession session) {
        if (isNotAdmin(session)) {
            return "redirect:/";
        }

        NqdOrder order = new NqdOrder();
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setTotalAmount(BigDecimal.ZERO);

        List<NqdUser> customers = userRepository.findByRole_Name("ROLE_USER");

        model.addAttribute("order", order);
        model.addAttribute("customers", customers);
        model.addAttribute("statuses", getOrderStatuses());
        model.addAttribute("paymentMethods", getPaymentMethods());
        model.addAttribute("pageTitle", "Thêm đơn hàng mới");

        return "admin/order-form";
    }

    // ========== FORM SỬA ==========
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id,
                               Model model,
                               HttpSession session) {
        if (isNotAdmin(session)) {
            return "redirect:/";
        }

        NqdOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng ID = " + id));

        List<NqdUser> customers = userRepository.findByRole_Name("ROLE_USER");

        model.addAttribute("order", order);
        model.addAttribute("customers", customers);
        model.addAttribute("statuses", getOrderStatuses());
        model.addAttribute("paymentMethods", getPaymentMethods());
        model.addAttribute("pageTitle", "Chỉnh sửa đơn hàng");

        return "admin/order-form";
    }

    // ========== LƯU (THÊM + SỬA) ==========
    @PostMapping("/save")
    public String saveOrder(@ModelAttribute("order") NqdOrder formOrder,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        if (isNotAdmin(session)) {
            return "redirect:/";
        }

        boolean isNew = (formOrder.getId() == null);

        if (isNew) {
            // nếu chưa set ngày đặt, lấy hiện tại
            if (formOrder.getOrderDate() == null) {
                formOrder.setOrderDate(LocalDateTime.now());
            }
            // nếu totalAmount null thì set 0
            if (formOrder.getTotalAmount() == null) {
                formOrder.setTotalAmount(BigDecimal.ZERO);
            }
            orderRepository.save(formOrder);
        } else {
            NqdOrder existing = orderRepository.findById(formOrder.getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

            // cập nhật các field cho phép sửa
            existing.setCustomer(formOrder.getCustomer());
            existing.setFullName(formOrder.getFullName());
            existing.setPhone(formOrder.getPhone());
            existing.setShipAddress(formOrder.getShipAddress());
            existing.setStatus(formOrder.getStatus());
            existing.setPaymentMethod(formOrder.getPaymentMethod());
            existing.setNote(formOrder.getNote());
            existing.setTotalAmount(formOrder.getTotalAmount());

            orderRepository.save(existing);
        }

        redirectAttributes.addFlashAttribute("successMessage", "Lưu đơn hàng thành công!");
        return "redirect:/admin/orders";
    }

    // ========== XOÁ ==========
    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable("id") Long id,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        if (isNotAdmin(session)) {
            return "redirect:/";
        }

        try {
            orderRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xoá đơn hàng ID = " + id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Không thể xoá đơn hàng (có thể vướng ràng buộc dữ liệu)");
        }

        return "redirect:/admin/orders";
    }
}
