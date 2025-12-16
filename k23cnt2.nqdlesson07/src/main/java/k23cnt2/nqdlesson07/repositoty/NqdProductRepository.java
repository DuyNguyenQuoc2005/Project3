package k23cnt2.nqdlesson07.repositoty;

import k23cnt2.nqdlesson07.entity.NqdProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface NqdProductRepository extends
        JpaRepository<NqdProduct, Long> {
}
