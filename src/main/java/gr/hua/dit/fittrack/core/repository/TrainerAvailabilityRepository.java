package gr.hua.dit.fittrack.core.repository;

import gr.hua.dit.fittrack.core.model.entity.TrainerAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TrainerAvailabilityRepository extends JpaRepository<TrainerAvailability, Long> {

    List<TrainerAvailability> findByTrainer(Long trainer);

    boolean existsByTrainerAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            Long trainer,
            LocalDateTime time,
            LocalDateTime time2
    );
}
