package k23cnt2.nqdproject3.repository;

import k23cnt2.nqdproject3.entity.NqdUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.List;
public interface NqdUserRepository extends JpaRepository<NqdUser, Long> {

    Optional<NqdUser> findByUsername(String username);

    boolean existsByUsername(String username);
    List<NqdUser> findByRole_Name(String roleName);


    @Query("""
    SELECT COUNT(u)
    FROM NqdUser u
    WHERE u.active = true
    AND u.role.name IN ('ROLE_ADMIN', 'ROLE_USER')
""")
    long countTotalUsers();

}
