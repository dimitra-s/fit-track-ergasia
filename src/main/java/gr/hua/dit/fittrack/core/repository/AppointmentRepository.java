package gr.hua.dit.fittrack.core.repository;

import gr.hua.dit.fittrack.core.model.entity.Appointment;
import gr.hua.dit.fittrack.core.model.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByUser_Id(Long userId);

    List<Appointment> findByTrainer_Id(Long trainerId);

    boolean existsByTrainer_IdAndDateTime(Long trainerId, LocalDateTime dateTime);
}


