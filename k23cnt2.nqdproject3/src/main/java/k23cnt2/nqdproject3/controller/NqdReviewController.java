package k23cnt2.nqdproject3.controller;

import jakarta.servlet.http.HttpSession;
import k23cnt2.nqdproject3.entity.NqdProduct;
import k23cnt2.nqdproject3.entity.NqdReview;
import k23cnt2.nqdproject3.entity.NqdUser;
import k23cnt2.nqdproject3.repository.NqdOrderDetailRepository;
import k23cnt2.nqdproject3.repository.NqdProductRepository;
import k23cnt2.nqdproject3.repository.NqdReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static k23cnt2.nqdproject3.controller.NqdAuthController.SESSION_USER_KEY;

@Controller
@RequestMapping("/favorites")   // trùng với link icon trái tim
@RequiredArgsConstructor
public class NqdReviewController {

    private final NqdOrderDetailRepository orderDetailRepository;
    private final NqdReviewRepository reviewRepository;
    private final NqdProductRepository productRepository;

    private NqdUser getLoggedUser(HttpSession session) {
        return (NqdUser) session.getAttribute(SESSION_USER_KEY);
    }

    // ========== TRANG ĐÁNH GIÁ ==========
    @GetMapping({"", "/"})
    public String showReviewPage(Model model, HttpSession session) {

        NqdUser user = getLoggedUser(session);
        if (user == null) {
            return "redirect:/account/login";
        }

        // Sản phẩm user đã mua
        List<NqdProduct> purchasedProducts =
                orderDetailRepository.findPurchasedProductsByCustomerId(user.getId());

        // Các review user đã viết
        List<NqdReview> reviews = reviewRepository.findByCustomer_Id(user.getId());
        Map<Long, NqdReview> reviewMap = reviews.stream()
                .collect(Collectors.toMap(r -> r.getProduct().getId(), Function.identity()));

        model.addAttribute("purchasedProducts", purchasedProducts);
        model.addAttribute("reviewMap", reviewMap);
        model.addAttribute("pageTitle", "Đánh giá sản phẩm");

        return "review/review-page";
    }

    // ========== LƯU / CẬP NHẬT ĐÁNH GIÁ ==========
    @PostMapping("/save")
    public String saveReview(@RequestParam("productId") Long productId,
                             @RequestParam("rating") Integer rating,
                             @RequestParam(value = "comment", required = false) String comment,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        NqdUser user = getLoggedUser(session);
        if (user == null) {
            return "redirect:/account/login";
        }

        if (rating == null || rating < 1 || rating > 5) {
            redirectAttributes.addFlashAttribute("errorMessage", "Điểm đánh giá phải từ 1 đến 5 sao.");
            return "redirect:/favorites";
        }

        NqdReview review = reviewRepository
                .findByCustomer_IdAndProduct_Id(user.getId(), productId)
                .orElseGet(NqdReview::new);

        review.setCustomer(user);
        review.setProduct(productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm")));
        review.setRating(rating);
        review.setComment(comment);

        reviewRepository.save(review);
        redirectAttributes.addFlashAttribute("successMessage", "Cảm ơn bạn đã đánh giá sản phẩm 🧡");

        return "redirect:/favorites";
    }
}
