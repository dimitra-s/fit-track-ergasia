package gr.hua.dit.fittrack.core.repository;

import gr.hua.dit.fittrack.core.model.TrainerNotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TrainerNotesRepository extends JpaRepository<TrainerNotes,Long> {

    List<TrainerNotes> findByAppointmentId(Long apointmentid);

    List<TrainerNotes> findByTrainerId(Long trainerid);
}
