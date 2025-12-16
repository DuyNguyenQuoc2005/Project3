package k23cnt2.nqdproject3.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import k23cnt2.nqdproject3.entity.NqdRole;
import k23cnt2.nqdproject3.entity.NqdUser;
import k23cnt2.nqdproject3.model.NqdLoginDto;
import k23cnt2.nqdproject3.model.NqdRegisterDto;
import k23cnt2.nqdproject3.repository.NqdRoleRepository;
import k23cnt2.nqdproject3.repository.NqdUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class NqdAuthController {

    public static final String SESSION_USER_KEY = "NQD_LOGGED_USER";

    private final NqdUserRepository userRepository;
    private final NqdRoleRepository roleRepository;

    // ========= ĐĂNG KÝ ==========

    @GetMapping("/account/register")
    public String showRegisterForm(Model model) {
        if (!model.containsAttribute("registerForm")) {
            model.addAttribute("registerForm", new NqdRegisterDto());
        }
        return "account/register";
    }

    @PostMapping("/account/register")
    public String doRegister(@Valid @ModelAttribute("registerForm") NqdRegisterDto form,
                             BindingResult bindingResult,
                             Model model,
                             HttpSession session) {

        // 1. Kiểm tra lỗi validate cơ bản
        if (bindingResult.hasErrors()) {
            return "account/register";
        }

        // 2. Kiểm tra password trùng confirm
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword",
                    "passwordMismatch",
                    "Mật khẩu nhập lại không khớp");
            return "account/register";
        }

        // 3. Kiểm tra trùng username
        if (userRepository.existsByUsername(form.getUsername())) {
            bindingResult.rejectValue("username",
                    "usernameExists",
                    "Tên đăng nhập đã tồn tại");
            return "account/register";
        }

        // 4. Lấy ROLE_USER (nếu chưa có thì tạo)
        NqdRole userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    NqdRole r = new NqdRole();
                    r.setName("ROLE_USER");
                    r.setDescription("Người dùng bình thường");
                    return roleRepository.save(r);
                });

        // 5. Tạo user mới
        NqdUser user = new NqdUser();
        user.setUsername(form.getUsername());
        // Lưu plain text cho bài tập (thực tế phải mã hóa)
        user.setPassword(form.getPassword());
        user.setFullName(form.getFullName());
        user.setEmail(form.getEmail());
        user.setPhone(form.getPhone());
        user.setAddress(form.getAddress());
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setRole(userRole);

        NqdUser saved = userRepository.save(user);

        // 6. Lưu user vào session (auto login)
        session.setAttribute(SESSION_USER_KEY, saved);

        // 7. Chuyển về trang chủ hoặc trang đơn hàng
        return "redirect:/";
    }

    // ========= ĐĂNG NHẬP ==========

    @GetMapping("/account/login")
    public String showLoginForm(Model model) {
        if (!model.containsAttribute("loginForm")) {
            model.addAttribute("loginForm", new NqdLoginDto());
        }
        return "account/login";
    }

    @PostMapping("/account/login")
    public String doLogin(@Valid @ModelAttribute("loginForm") NqdLoginDto form,
                          BindingResult bindingResult,
                          HttpSession session,
                          Model model) {

        if (bindingResult.hasErrors()) {
            return "account/login";
        }

        NqdUser user = userRepository.findByUsername(form.getUsername())
                .orElse(null);

        if (user == null || !user.getPassword().equals(form.getPassword())) {
            bindingResult.reject("loginFailed", "Sai tên đăng nhập hoặc mật khẩu");
            return "account/login";
        }

        if (Boolean.FALSE.equals(user.getActive())) {
            bindingResult.reject("inactive", "Tài khoản đã bị khóa, liên hệ admin");
            return "account/login";
        }

        // Đăng nhập OK
        session.setAttribute(SESSION_USER_KEY, user);

        // Sau khi login, cho quay về trang đơn hàng cá nhân
        return "redirect:/";
    }

    // ========= ĐĂNG XUẤT ==========

    @GetMapping("/account/logout")
    public String logout(HttpSession session) {
        session.removeAttribute(SESSION_USER_KEY);
        return "redirect:/";
    }
}
