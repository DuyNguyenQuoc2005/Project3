package k23cnt2.nqdproject3.controller;

import k23cnt2.nqdproject3.entity.NqdProduct;
import k23cnt2.nqdproject3.repository.NqdCategoryRepository;
import k23cnt2.nqdproject3.repository.NqdProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
public class NqdProductController {

    private final NqdProductRepository productRepository;
    private final NqdCategoryRepository categoryRepository;

    // ------------------ DANH SÁCH SẢN PHẨM ------------------
    @GetMapping("/products")
    public String listProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String sort,
            Model model
    ) {
        int pageSize = 9;

        // Giá mặc định nếu không truyền
        BigDecimal min = (minPrice != null) ? minPrice : BigDecimal.ZERO;
        BigDecimal max = (maxPrice != null) ? maxPrice : BigDecimal.valueOf(1_000_000);

        // Xử lý sort
        Sort sortSpec;
        if ("priceAsc".equals(sort)) {
            sortSpec = Sort.by("price").ascending();
        } else if ("priceDesc".equals(sort)) {
            sortSpec = Sort.by("price").descending();
        } else if ("nameAsc".equals(sort)) {
            sortSpec = Sort.by("name").ascending();
        } else {
            // mặc định: id mới nhất trước
            sortSpec = Sort.by("id").descending();
            sort = "default";
        }

        Pageable pageable = PageRequest.of(page, pageSize, sortSpec);

        // Gọi repository tuỳ theo có categoryId hay không
        Page<NqdProduct> productPage;
        if (categoryId != null) {
            productPage = productRepository
                    .findByActiveTrueAndCategory_IdAndPriceBetween(categoryId, min, max, pageable);
        } else {
            productPage = productRepository
                    .findByActiveTrueAndPriceBetween(min, max, pageable);
        }

        // Đẩy dữ liệu ra view
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", productPage.getNumber());
        model.addAttribute("totalPages", productPage.getTotalPages());

        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("selectedCategoryId", categoryId);

        model.addAttribute("minPrice", min);
        model.addAttribute("maxPrice", max);
        model.addAttribute("sort", sort);

        return "product/list";
    }

    // ------------------ CHI TIẾT SẢN PHẨM ------------------
    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        NqdProduct product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        model.addAttribute("product", product);
        return "product/detail";
    }
}
