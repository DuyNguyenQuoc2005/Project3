package k23cnt2.nqdproject3.repository;

import k23cnt2.nqdproject3.entity.NqdProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface NqdProductRepository extends JpaRepository<NqdProduct, Long> {

    // Lấy tất cả sản phẩm đang active (nếu còn dùng ở chỗ khác)
    List<NqdProduct> findByActiveTrue();

    // Tìm theo category (nếu còn dùng ở chỗ khác)
    List<NqdProduct> findByCategory_IdAndActiveTrue(Long categoryId);

    // Lọc theo giá (không filter category)
    Page<NqdProduct> findByActiveTrueAndPriceBetween(BigDecimal minPrice,
                                                     BigDecimal maxPrice,
                                                     Pageable pageable);

    // Lọc theo category + giá
    Page<NqdProduct> findByActiveTrueAndCategory_IdAndPriceBetween(Long categoryId,
                                                                   BigDecimal minPrice,
                                                                   BigDecimal maxPrice,
                                                                   Pageable pageable);
    boolean existsBySlug(String slug);

    long countByActiveTrue();

}
