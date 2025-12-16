package k23cnt2.nqdproject3.repository;

import k23cnt2.nqdproject3.entity.NqdOrderDetail;
import k23cnt2.nqdproject3.entity.NqdProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NqdOrderDetailRepository extends JpaRepository<NqdOrderDetail, Long> {

    @Query("select distinct od.product " +
            "from NqdOrderDetail od " +
            "where od.order.customer.id = :customerId")
    List<NqdProduct> findPurchasedProductsByCustomerId(@Param("customerId") Long customerId);
}
