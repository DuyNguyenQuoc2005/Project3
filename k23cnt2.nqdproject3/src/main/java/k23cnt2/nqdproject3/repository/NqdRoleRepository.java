package k23cnt2.nqdproject3.repository;

import k23cnt2.nqdproject3.entity.NqdRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NqdRoleRepository extends JpaRepository<NqdRole, Long> {

    Optional<NqdRole> findByName(String name);
}
