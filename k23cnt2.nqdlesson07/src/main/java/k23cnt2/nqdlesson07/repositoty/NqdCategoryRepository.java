package k23cnt2.nqdlesson07.repositoty;


import k23cnt2.nqdlesson07.entity.NqdCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NqdCategoryRepository extends
        JpaRepository<NqdCategory, Long> {
}
