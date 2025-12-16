package k23cnt2.nqdproject3.repository;

import k23cnt2.nqdproject3.entity.NqdOrder;
import k23cnt2.nqdproject3.entity.NqdUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NqdOrderRepository extends JpaRepository<NqdOrder, Long> {

    List<NqdOrder> findByCustomerOrderByOrderDateDesc(NqdUser customer);

    long countByStatus(String status);
}

