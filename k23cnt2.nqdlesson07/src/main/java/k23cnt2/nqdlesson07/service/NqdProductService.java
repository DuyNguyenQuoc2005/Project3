package k23cnt2.nqdlesson07.service;

import k23cnt2.nqdlesson07.entity.NqdCategory;
import k23cnt2.nqdlesson07.entity.NqdProduct;
import k23cnt2.nqdlesson07.repositoty.NqdProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
public class NqdProductService {
    @Autowired
    private NqdProductRepository productRepository;
    // Đọc toàn bộ dữ liệu bảng Product
    public List<NqdProduct> getAllProducts() {
        return productRepository.findAll();
    }
    // Đọc dữ liệu bảng Product theo id
    public Optional<NqdProduct> findById(Long id) {
        return productRepository.findById(id);
    }
    // Cập nhật: create / update
    public NqdProduct saveProduct(NqdProduct product) {
        System.out.println(product);
        return productRepository.save(product);
    }
    // Xóa product theo id
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
