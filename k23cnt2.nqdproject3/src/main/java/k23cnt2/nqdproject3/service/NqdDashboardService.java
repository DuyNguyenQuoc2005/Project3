package k23cnt2.nqdproject3.service;

import k23cnt2.nqdproject3.repository.NqdOrderRepository;
import k23cnt2.nqdproject3.repository.NqdProductRepository;
import k23cnt2.nqdproject3.repository.NqdReviewRepository;
import k23cnt2.nqdproject3.repository.NqdUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NqdDashboardService {

    private final NqdUserRepository userRepo;
    private final NqdProductRepository productRepo;
    private final NqdOrderRepository orderRepo;
    private final NqdReviewRepository reviewRepo;

    public long getTotalUsers() {
        return userRepo.countTotalUsers();
    }

    public long getTotalProducts() {
        return productRepo.countByActiveTrue();
    }

    public long getNewOrders() {
        return orderRepo.countByStatus("PENDING");
    }

    public long getTotalReviews() {
        return reviewRepo.count();
    }
}
