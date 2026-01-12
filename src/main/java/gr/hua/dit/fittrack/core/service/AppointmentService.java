package gr.hua.dit.fittrack.core.service;

import gr.hua.dit.fittrack.core.model.entity.Appointment;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAppointmentRequest;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAppointmentResult;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {

    // Δημιουργία ραντεβού
    CreateAppointmentResult createAppointment(CreateAppointmentRequest req, boolean notify);

    default CreateAppointmentResult createAppointment(CreateAppointmentRequest req) {
        return createAppointment(req, false);
    }

    // Διαγραφή ραντεβού
    void deleteAppointment(Long id);

    // Εύρεση συγκεκριμένου ραντεβού (για τη σελίδα σημειώσεων)
    Appointment findById(Long id);

    // Λίστα ραντεβού για Trainer βάσει email
    List<Appointment> getAppointmentsForTrainer(String email);

    List<Appointment> getAppointmentsByTrainer(String trainerEmail);
    List<Appointment> getAppointmentsByUser(String userEmail); // Πρόσθεσε και αυτήν αν λείπει

    // ✅ Λειτουργία: Αποδοχή/Ακύρωση ραντεβού
    void updateStatus(Long id, String status);

    // ✅ Λειτουργία: Καταγραφή σημειώσεων/πλάνου προπόνησης
    void updateNotes(Long id, String notes);

    void cancelAppointment(Long appointmentId);

    // Διαχείριση διαθεσιμότητας
    List<LocalDateTime> getAvailableSlots(Long trainerId);
    void setTrainerAvailability(Long trainerId, LocalDateTime start, LocalDateTime end);
}