package gr.hua.dit.fittrack.core.repository;

import gr.hua.dit.fittrack.core.model.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Για να βλέπει ο χρήστης τα ραντεβού του (μέσω ID)
    List<Appointment> findByUser_Id(Long userId);

    // Για να βλέπει ο χρήστης τα ραντεβού του (μέσω Email/Username - πολύ χρήσιμο για το Security)
    List<Appointment> findByUser_EmailAddress(String email);

    // Για να βλέπει ο Trainer το πρόγραμμά του
    List<Appointment> findByTrainer_Id(Long trainerId);

    // Ο κανόνας logic: Έλεγχος αν ο trainer έχει ήδη ραντεβού τη συγκεκριμένη ώρα
    boolean existsByTrainer_IdAndDateTime(Long trainerId, LocalDateTime dateTime);

    // Πρόσθετο: Έλεγχος αν ο Χρήστης έχει ήδη ραντεβού την ίδια ώρα (για να μην κλείνει διπλά)
    boolean existsByUser_IdAndDateTime(Long userId, LocalDateTime dateTime);
}