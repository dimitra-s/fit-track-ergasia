package gr.hua.dit.fittrack.core.repository;

import gr.hua.dit.fittrack.core.model.ProgressRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgressRepository extends JpaRepository<ProgressRecord, Long> {

    List<ProgressRecord> findByUserid(Long userid);

    List<ProgressRecord> findByUseridAndDate(Long userid, String date);
}
