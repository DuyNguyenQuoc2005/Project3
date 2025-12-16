package k23cnt2.nqdproject3.controller;

import jakarta.servlet.http.HttpSession;
import k23cnt2.nqdproject3.entity.NqdReview;
import k23cnt2.nqdproject3.entity.NqdUser;
import k23cnt2.nqdproject3.repository.NqdReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static k23cnt2.nqdproject3.controller.NqdAuthController.SESSION_USER_KEY;

@Controller
@RequestMapping("/admin/feedbacks")
@RequiredArgsConstructor
public class NqdAdminFeedbackController {

    private final NqdReviewRepository reviewRepository;

    // Check admin
    private boolean isNotAdmin(HttpSession session) {
        Object obj = session.getAttribute(SESSION_USER_KEY);
        if (!(obj instanceof NqdUser user)) {
            return true;
        }
        return user.getRole() == null
                || user.getRole().getName() == null
                || !user.getRole().getName().equalsIgnoreCase("ROLE_ADMIN");
    }

    // ========== LIST GÓP Ý ==========
    @GetMapping({"", "/"})
    public String listFeedbacks(Model model, HttpSession session) {

        if (isNotAdmin(session)) {
            return "redirect:/";
        }

        List<NqdReview> reviews =
                reviewRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));

        model.addAttribute("reviews", reviews);
        model.addAttribute("pageTitle", "Quản lý góp ý");

        return "admin/feedback-list";
    }

    // ========== FORM SỬA GÓP Ý ==========
    @GetMapping("/edit/{id}")
    public String editFeedback(@PathVariable("id") Long id,
                               Model model,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {

        if (isNotAdmin(session)) {
            return "redirect:/";
        }

        NqdReview review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy góp ý ID = " + id));

        model.addAttribute("review", review);
        model.addAttribute("pageTitle", "Chỉnh sửa góp ý");

        return "admin/feedback-form";
    }

    // ========== LƯU SỬA ==========
    @PostMapping("/save")
    public String saveFeedback(@ModelAttribute("review") NqdReview formReview,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {

        if (isNotAdmin(session)) {
            return "redirect:/";
        }

        NqdReview existing = reviewRepository.findById(formReview.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy góp ý"));

        // Cho admin chỉnh rating + nội dung góp ý (nếu cần)
        existing.setRating(formReview.getRating());
        existing.setComment(formReview.getComment());

        reviewRepository.save(existing);

        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật góp ý thành công!");
        return "redirect:/admin/feedbacks";
    }

    // ========== XOÁ ==========
    @GetMapping("/delete/{id}")
    public String deleteFeedback(@PathVariable("id") Long id,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {

        if (isNotAdmin(session)) {
            return "redirect:/";
        }

        try {
            reviewRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Đã xoá góp ý ID = " + id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Không thể xoá góp ý.");
        }

        return "redirect:/admin/feedbacks";
    }
}
