package k23cnt2.nqdproject3.repository;

import k23cnt2.nqdproject3.entity.NqdReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NqdReviewRepository extends JpaRepository<NqdReview, Long> {

    List<NqdReview> findByCustomer_Id(Long customerId);

    Optional<NqdReview> findByCustomer_IdAndProduct_Id(Long customerId, Long productId);
}
