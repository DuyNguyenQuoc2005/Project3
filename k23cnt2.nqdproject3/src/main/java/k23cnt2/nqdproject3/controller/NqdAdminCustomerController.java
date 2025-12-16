package k23cnt2.nqdproject3.controller;

import jakarta.servlet.http.HttpSession;
import k23cnt2.nqdproject3.entity.NqdRole;
import k23cnt2.nqdproject3.entity.NqdUser;
import k23cnt2.nqdproject3.repository.NqdRoleRepository;
import k23cnt2.nqdproject3.repository.NqdUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static k23cnt2.nqdproject3.controller.NqdAuthController.SESSION_USER_KEY;

@Controller
@RequestMapping("/admin/customers")
@RequiredArgsConstructor
public class NqdAdminCustomerController {

    private final NqdUserRepository userRepository;
    private final NqdRoleRepository roleRepository;

    // Check admin giống bên Product
    private boolean isNotAdmin(HttpSession session) {
        Object obj = session.getAttribute(SESSION_USER_KEY);
        if (!(obj instanceof k23cnt2.nqdproject3.entity.NqdUser user)) {
            return true;
        }
        return user.getRole() == null
                || user.getRole().getName() == null
                || !user.getRole().getName().equalsIgnoreCase("ROLE_ADMIN");
    }

    // ========== LIST KHÁCH HÀNG ==========
    @GetMapping({"", "/"})
    public String listCustomers(Model model, HttpSession session) {
        if (isNotAdmin(session)) {
            return "redirect:/";
        }

        // Chỉ lấy user có role ROLE_USER (khách hàng)
        List<NqdUser> customers = userRepository.findByRole_Name("ROLE_USER");

        model.addAttribute("customers", customers);
        model.addAttribute("pageTitle", "Quản lý khách hàng");

        return "admin/customer-list";
    }

    // ========== FORM THÊM ==========
    @GetMapping("/new")
    public String showCreateForm(Model model, HttpSession session) {
        if (isNotAdmin(session)) {
            return "redirect:/";
        }

        NqdUser customer = new NqdUser();
        customer.setActive(true); // default đang hoạt động

        model.addAttribute("customer", customer);
        model.addAttribute("pageTitle", "Thêm khách hàng mới");
        return "admin/customer-form";
    }

    // ========== FORM SỬA ==========
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id,
                               Model model,
                               HttpSession session) {
        if (isNotAdmin(session)) {
            return "redirect:/";
        }

        NqdUser customer = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng ID = " + id));

        model.addAttribute("customer", customer);
        model.addAttribute("pageTitle", "Chỉnh sửa khách hàng");
        return "admin/customer-form";
    }

    // ========== LƯU (THÊM + SỬA) ==========
    @PostMapping("/save")
    public String saveCustomer(@ModelAttribute("customer") NqdUser formCustomer,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        if (isNotAdmin(session)) {
            return "redirect:/";
        }

        boolean isNew = (formCustomer.getId() == null);

        if (isNew) {
            // Set role khách hàng
            NqdRole userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Chưa cấu hình ROLE_USER"));

            formCustomer.setRole(userRole);

            if (formCustomer.getActive() == null) {
                formCustomer.setActive(true);
            }

            // TODO: nếu cậu có passwordEncoder thì encode ở đây
            userRepository.save(formCustomer);
        } else {
            // Update
            NqdUser existing = userRepository.findById(formCustomer.getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

            existing.setFullName(formCustomer.getFullName());
            existing.setEmail(formCustomer.getEmail());
            existing.setPhone(formCustomer.getPhone());
            existing.setAddress(formCustomer.getAddress());
            existing.setActive(formCustomer.getActive());

            // Nếu password form không rỗng => đổi mật khẩu
            if (StringUtils.hasText(formCustomer.getPassword())) {
                existing.setPassword(formCustomer.getPassword());
            }

            // Username & role giữ nguyên
            userRepository.save(existing);
        }

        redirectAttributes.addFlashAttribute("successMessage", "Lưu thông tin khách hàng thành công!");
        return "redirect:/admin/customers";
    }

    // ========== XOÁ ==========
    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable("id") Long id,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {

        if (isNotAdmin(session)) {
            return "redirect:/";
        }

        try {
            NqdUser user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

            // Không cho xoá admin lỡ mà list lẫn
            if (user.getRole() != null
                    && "ROLE_ADMIN".equalsIgnoreCase(user.getRole().getName())) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Không thể xoá tài khoản quản trị!");
                return "redirect:/admin/customers";
            }

            userRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xoá khách hàng ID = " + id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Không thể xoá khách hàng (đã tồn tại trong đơn hàng?)");
        }

        return "redirect:/admin/customers";
    }
}
