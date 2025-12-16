package k23cnt2.nqdproject3.config;

import k23cnt2.nqdproject3.entity.*;
import k23cnt2.nqdproject3.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NqdDataInitializer implements CommandLineRunner {

    private final NqdRoleRepository roleRepository;
    private final NqdUserRepository userRepository;
    private final NqdCategoryRepository categoryRepository;
    private final NqdProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        initRoles();
        initAdminUser();
        initCategoriesAndProducts();
    }

    private void initRoles() {
        if (roleRepository.count() == 0) {
            NqdRole adminRole = NqdRole.builder()
                    .name("ROLE_ADMIN")
                    .description("Quản trị hệ thống")
                    .build();

            NqdRole userRole = NqdRole.builder()
                    .name("ROLE_USER")
                    .description("Khách hàng")
                    .build();

            roleRepository.saveAll(List.of(adminRole, userRole));
        }
    }

    private void initAdminUser() {
        if (userRepository.count() == 0) {
            NqdRole adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy ROLE_ADMIN"));

            NqdUser admin = NqdUser.builder()
                    .username("admin")
                    .password("123456") // Tạm để plain text, sau này dùng Spring Security sẽ mã hoá
                    .fullName("Admin Bánh Trung Thu")
                    .email("admin@banhtrungthu.com")
                    .phone("0123456789")
                    .address("TP.HCM")
                    .active(true)
                    .createdAt(LocalDateTime.now())
                    .role(adminRole)
                    .build();

            userRepository.save(admin);
        }
    }

    private void initCategoriesAndProducts() {
        if (categoryRepository.count() == 0 && productRepository.count() == 0) {
            // Tạo category
            NqdCategory catNuong = NqdCategory.builder()
                    .name("Bánh nướng")
                    .description("Các loại bánh nướng truyền thống")
                    .active(true)
                    .build();

            NqdCategory catDeo = NqdCategory.builder()
                    .name("Bánh dẻo")
                    .description("Các loại bánh dẻo truyền thống")
                    .active(true)
                    .build();

            NqdCategory catCombo = NqdCategory.builder()
                    .name("Combo hộp quà")
                    .description("Combo hộp quà tặng Trung Thu")
                    .active(true)
                    .build();

            categoryRepository.saveAll(List.of(catNuong, catDeo, catCombo));

            // Tạo một số sản phẩm mẫu
            NqdProduct p1 = NqdProduct.builder()
                    .category(catNuong)
                    .name("Bánh nướng thập cẩm 2 trứng 230g")
                    .slug("banh-nuong-thap-cam-2-trung-230g")
                    .price(new BigDecimal("120000"))
                    .originalPrice(new BigDecimal("135000"))
                    .weight(230)
                    .eggCount(2)
                    .flavor("Thập cẩm")
                    .imageUrl("/images/banh-nuong-thap-cam-2-trung.jpg")
                    .shortDescription("Bánh nướng thập cẩm 2 trứng muối, vị truyền thống")
                    .description("Nhân thập cẩm với lạp xưởng, jambon, mứt bí, hạt điều...")
                    .newProduct(true)
                    .bestSeller(true)
                    .active(true)
                    .stockQuantity(100)
                    .build();

            NqdProduct p2 = NqdProduct.builder()
                    .category(catDeo)
                    .name("Bánh dẻo đậu xanh 1 trứng 200g")
                    .slug("banh-deo-dau-xanh-1-trung-200g")
                    .price(new BigDecimal("90000"))
                    .weight(200)
                    .eggCount(1)
                    .flavor("Đậu xanh")
                    .imageUrl("/images/banh-deo-dau-xanh-1-trung.jpg")
                    .shortDescription("Bánh dẻo đậu xanh 1 trứng muối")
                    .description("Nhân đậu xanh mềm mịn, thơm béo, kết hợp trứng muối mặn mà.")
                    .newProduct(true)
                    .bestSeller(false)
                    .active(true)
                    .stockQuantity(80)
                    .build();

            NqdProduct p3 = NqdProduct.builder()
                    .category(catCombo)
                    .name("Combo hộp quà Trung Thu 4 bánh thượng hạng")
                    .slug("combo-hop-qua-4-banh-thuong-hang")
                    .price(new BigDecimal("550000"))
                    .originalPrice(new BigDecimal("600000"))
                    .weight(920) // 4 bánh 230g
                    .eggCount(4)
                    .flavor("Nhiều hương vị")
                    .imageUrl("/images/combo-4-banh-thuong-hang.jpg")
                    .shortDescription("Hộp quà 4 bánh thượng hạng, thích hợp làm quà biếu.")
                    .description("Bao gồm 4 bánh thập cẩm, jambon, hạt sen, trà xanh...")
                    .newProduct(false)
                    .bestSeller(true)
                    .active(true)
                    .stockQuantity(50)
                    .build();

            productRepository.saveAll(List.of(p1, p2, p3));
        }
    }
}
