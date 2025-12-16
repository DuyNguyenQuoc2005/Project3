package k23cnt2.nqdlesson07.service;

import k23cnt2.nqdlesson07.entity.NqdCategory;
import k23cnt2.nqdlesson07.repositoty.NqdCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NqdCategoryService {

    private final NqdCategoryRepository nqdCategoryRepository;

    @Autowired
    public NqdCategoryService(NqdCategoryRepository nqdCategoryRepository) {
        this.nqdCategoryRepository = nqdCategoryRepository;
    }

    // Lấy danh sách
    public List<NqdCategory> getAllCategories() {
        System.out.println(nqdCategoryRepository.findAll());
        return nqdCategoryRepository.findAll();
    }

    // Lấy category theo id
    public Optional<NqdCategory> getCategoryById(Long id) {
        return nqdCategoryRepository.findById(id);
    }

    // Cập nhật dữ liệu bảng category: create / update
    public NqdCategory saveCategory(NqdCategory category) {
        return nqdCategoryRepository.save(category);
    }

    // Xóa category theo id
    public void deleteCategory(Long id) {
        nqdCategoryRepository.deleteById(id);
    }
}
