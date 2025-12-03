package gr.hua.dit.fittrack.core.model.repository;

import gr.hua.dit.fittrack.core.model.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByUserId(Long userId);

    List<Appointment> findByTrainerId(Long trainerId);
}

