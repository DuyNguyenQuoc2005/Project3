package k23cnt2.nqdproject3.controller;

import jakarta.servlet.http.HttpSession;
import k23cnt2.nqdproject3.entity.NqdProduct;
import k23cnt2.nqdproject3.entity.NqdUser;
import k23cnt2.nqdproject3.repository.NqdCategoryRepository;
import k23cnt2.nqdproject3.repository.NqdProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static k23cnt2.nqdproject3.controller.NqdAuthController.SESSION_USER_KEY;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class NqdAdminProductController {

    private final NqdProductRepository productRepository;
    private final NqdCategoryRepository categoryRepository;

    // ================== HÀM CHECK ADMIN ==================
    private boolean isNotAdmin(HttpSession session) {
        NqdUser user = (NqdUser) session.getAttribute(SESSION_USER_KEY);
        if (user == null || user.getRole() == null || user.getRole().getName() == null) {
            return true;
        }
        return !user.getRole().getName().equalsIgnoreCase("ROLE_ADMIN");
    }

    // ================== LIST SẢN PHẨM ==================
    @GetMapping({"", "/"})
    public String listProducts(Model model, HttpSession session) {

        if (isNotAdmin(session)) {
            return "redirect:/";
        }

        List<NqdProduct> products = productRepository.findAll();
        model.addAttribute("products", products);
        model.addAttribute("pageTitle", "Quản lý sản phẩm");

        return "admin/product-list";
    }

    // ================== FORM THÊM MỚI ==================
    @GetMapping("/new")
    public String showCreateForm(Model model, HttpSession session) {
        if (isNotAdmin(session)) {
            return "redirect:/";
        }

        NqdProduct product = new NqdProduct();
        // mặc định đang bán
        product.setActive(true);

        model.addAttribute("product", product);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("pageTitle", "Thêm sản phẩm mới");

        return "admin/product-form";
    }

    // ================== FORM SỬA ==================
    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable("id") Long id,
                              HttpSession session,
                              Model model) {

        if (isNotAdmin(session)) {
            return "redirect:/";
        }

        NqdProduct product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm ID=" + id));

        model.addAttribute("product", product);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("pageTitle", "Chỉnh sửa sản phẩm");

        // dùng chung form với thêm mới
        return "admin/product-form";
    }

    // ================== LƯU (THÊM + SỬA) ==================
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") NqdProduct product,
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {

        if (isNotAdmin(session)) {
            return "redirect:/";
        }

        try {
            // --------- XỬ LÝ ẢNH UPLOAD ---------
            if (imageFile != null && !imageFile.isEmpty()) {
                String originalName = StringUtils.cleanPath(imageFile.getOriginalFilename());
                String fileName = System.currentTimeMillis() + "-" + originalName;

                Path uploadDir = Paths.get("uploads");
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }

                Path dest = uploadDir.resolve(fileName);
                Files.copy(imageFile.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);

                // set lại imageUrl để front-end dùng
                product.setImageUrl("/uploads/" + fileName);
            }
            // Nếu KHÔNG upload mà người dùng nhập URL ảnh tay
            // => product.getImageUrl() đã có giá trị từ form, giữ nguyên

            // --------- XỬ LÝ SLUG ĐỂ KHỎI LỖI UNIQUE '' ---------
            // Nếu slug trống hoặc chỉ toàn khoảng trắng -> set về null
            if (!StringUtils.hasText(product.getSlug())) {
                product.setSlug(null);
            }

            // --------- LƯU DB ---------
            productRepository.save(product);
            redirectAttributes.addFlashAttribute("successMessage", "Lưu sản phẩm thành công!");

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi khi upload ảnh!");
        }

        return "redirect:/admin/products";
    }

    // ================== XOÁ SẢN PHẨM ==================
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        if (isNotAdmin(session)) {
            return "redirect:/";
        }

        try {
            productRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xoá sản phẩm ID = " + id);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Không thể xoá sản phẩm (có thể đang được dùng trong đơn hàng)");
        }

        return "redirect:/admin/products";
    }
}
