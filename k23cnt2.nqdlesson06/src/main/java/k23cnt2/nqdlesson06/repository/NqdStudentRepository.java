package k23cnt2.nqdlesson06.repository;

import k23cnt2.nqdlesson06.entity.NqdStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NqdStudentRepository extends
        JpaRepository<NqdStudent, Long> {
}