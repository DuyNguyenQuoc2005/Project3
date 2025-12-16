package k23cnt2.nqdproject3.controller;

import jakarta.servlet.http.HttpSession;
import k23cnt2.nqdproject3.entity.NqdUser;
import k23cnt2.nqdproject3.service.NqdDashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static k23cnt2.nqdproject3.controller.NqdAuthController.SESSION_USER_KEY;

@Controller
@RequestMapping("/admin")
public class NqdAdminController {

    // ✅ THÊM DÒNG NÀY
    private final NqdDashboardService dashboardService;

    // ✅ THÊM CONSTRUCTOR (constructor injection)
    public NqdAdminController(NqdDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    // GET /admin  hoặc /admin/dashboard đều vào đây
    @GetMapping({"", "/", "/dashboard"})
    public String dashboard(Model model, HttpSession session) {

        // Lấy user trong session
        NqdUser user = (NqdUser) session.getAttribute(SESSION_USER_KEY);

        // Nếu chưa login -> đá ra trang chủ
        if (user == null) {
            return "redirect:/";
        }

        // Nếu không phải admin -> cũng đá ra trang chủ
        if (user.getRole() == null
                || user.getRole().getName() == null
                || !user.getRole().getName().equalsIgnoreCase("ROLE_ADMIN")) {
            return "redirect:/";
        }

        model.addAttribute("pageTitle", "Trang quản trị");
        // === DỮ LIỆU DASHBOARD ===
        model.addAttribute("totalUsers", dashboardService.getTotalUsers());
        model.addAttribute("totalProducts", dashboardService.getTotalProducts());
        model.addAttribute("newOrders", dashboardService.getNewOrders());
        model.addAttribute("newFeedbacks", dashboardService.getTotalReviews());

        return "admin/dashboard";
    }
}
