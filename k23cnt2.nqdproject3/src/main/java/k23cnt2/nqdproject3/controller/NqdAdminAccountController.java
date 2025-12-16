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
@RequestMapping("/admin/accounts")
@RequiredArgsConstructor
public class NqdAdminAccountController {

    private final NqdUserRepository userRepository;
    private final NqdRoleRepository roleRepository;

    // Check admin giống các controller khác
    private boolean isNotAdmin(HttpSession session) {
        Object obj = session.getAttribute(SESSION_USER_KEY);
        if (!(obj instanceof NqdUser user)) {
            return true;
        }
        return user.getRole() == null
                || user.getRole().getName() == null
                || !user.getRole().getName().equalsIgnoreCase("ROLE_ADMIN");
    }

    // ========== LIST TÀI KHOẢN ADMIN ==========
    @GetMapping({"", "/"})
    public String listAdmins(Model model, HttpSession session) {

        if (isNotAdmin(session)) {
            return "redirect:/";
        }

        // chỉ lấy user có ROLE_ADMIN
        List<NqdUser> admins = userRepository.findByRole_Name("ROLE_ADMIN");

        model.addAttribute("admins", admins);
        model.addAttribute("pageTitle", "Quản lý tài khoản Admin");

        return "admin/admin-account-list";
    }

    // ========== FORM THÊM ADMIN ==========
    @GetMapping("/new")
    public String showCreateForm(Model model, HttpSession session) {

        if (isNotAdmin(session)) {
            return "redirect:/";
        }

        NqdUser admin = new NqdUser();
        admin.setActive(true);

        model.addAttribute("admin", admin);
        model.addAttribute("pageTitle", "Thêm tài khoản Admin");

        return "admin/admin-account-form";
    }

    // ========== FORM SỬA ADMIN ==========
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id,
                               Model model,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {

        if (isNotAdmin(session)) {
            return "redirect:/";
        }

        NqdUser admin = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy admin ID = " + id));

        // đảm bảo chắc chắn là ROLE_ADMIN
        if (admin.getRole() == null
                || admin.getRole().getName() == null
                || !admin.getRole().getName().equalsIgnoreCase("ROLE_ADMIN")) {

            redirectAttributes.addFlashAttribute("errorMessage", "Tài khoản này không phải Admin.");
            return "redirect:/admin/accounts";
        }

        model.addAttribute("admin", admin);
        model.addAttribute("pageTitle", "Chỉnh sửa tài khoản Admin");

        return "admin/admin-account-form";
    }

    // ========== LƯU (THÊM + SỬA) ==========
    @PostMapping("/save")
    public String saveAdmin(@ModelAttribute("admin") NqdUser formAdmin,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        if (isNotAdmin(session)) {
            return "redirect:/";
        }

        boolean isNew = (formAdmin.getId() == null);

        // Lấy ROLE_ADMIN
        NqdRole adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("Chưa cấu hình ROLE_ADMIN trong hệ thống"));

        if (isNew) {
            // tạo mới
            formAdmin.setRole(adminRole);

            if (formAdmin.getActive() == null) {
                formAdmin.setActive(true);
            }

            if (!StringUtils.hasText(formAdmin.getPassword())) {
                throw new RuntimeException("Mật khẩu không được để trống khi thêm Admin mới");
            }

            // TODO: nếu có passwordEncoder thì encode ở đây
            userRepository.save(formAdmin);
        } else {
            // update
            NqdUser existing = userRepository.findById(formAdmin.getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy admin"));

            existing.setFullName(formAdmin.getFullName());
            existing.setEmail(formAdmin.getEmail());
            existing.setPhone(formAdmin.getPhone());
            existing.setAddress(formAdmin.getAddress());
            existing.setActive(formAdmin.getActive());

            // username và role không cho sửa
            existing.setRole(adminRole);

            // đổi mật khẩu nếu user nhập mới
            if (StringUtils.hasText(formAdmin.getPassword())) {
                existing.setPassword(formAdmin.getPassword());
            }

            userRepository.save(existing);
        }

        redirectAttributes.addFlashAttribute("successMessage", "Lưu tài khoản Admin thành công!");
        return "redirect:/admin/accounts";
    }

    // ========== XOÁ ADMIN ==========
    @GetMapping("/delete/{id}")
    public String deleteAdmin(@PathVariable("id") Long id,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {

        if (isNotAdmin(session)) {
            return "redirect:/";
        }

        NqdUser current = (NqdUser) session.getAttribute(SESSION_USER_KEY);

        // Không cho tự xoá chính mình cho đỡ toang
        if (current != null && current.getId().equals(id)) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Không thể xoá tài khoản đang đăng nhập.");
            return "redirect:/admin/accounts";
        }

        try {
            NqdUser admin = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy admin"));

            if (admin.getRole() == null
                    || admin.getRole().getName() == null
                    || !admin.getRole().getName().equalsIgnoreCase("ROLE_ADMIN")) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Tài khoản này không phải Admin.");
                return "redirect:/admin/accounts";
            }

            userRepository.delete(admin);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Đã xoá tài khoản Admin ID = " + id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Không thể xoá tài khoản Admin.");
        }

        return "redirect:/admin/accounts";
    }
}
