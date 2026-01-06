package gr.hua.dit.fittrack.core.repository;

import gr.hua.dit.fittrack.core.model.entity.ProgressRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProgressRepository extends JpaRepository<ProgressRecord, Long> {

    List<ProgressRecord> findByUser_Id(Long userId);

    List<ProgressRecord> findByUser_IdAndDate(Long userId, LocalDate date);
}

