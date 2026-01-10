package gr.hua.dit.fittrack.web.controller;

import gr.hua.dit.fittrack.core.service.AppointmentService;
import gr.hua.dit.fittrack.core.service.TrainerService;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAppointmentRequest;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAppointmentResult;
import gr.hua.dit.fittrack.core.repository.TrainerRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/appointments")
public class AppointmentViewController {

    private final AppointmentService appointmentService;
    private final TrainerService trainerService;

    public AppointmentViewController(
            final AppointmentService appointmentService,
            final TrainerService trainerService) {
        if (appointmentService == null) throw new NullPointerException();
        if (trainerService == null) throw new NullPointerException();

        this.appointmentService = appointmentService;
        this.trainerService = trainerService;
    }

    // 1. Εμφάνιση φόρμας νέου ραντεβού
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        // Χρησιμοποιούμε τον νέο constructor με default τιμές
        model.addAttribute("appointmentRequest", new CreateAppointmentRequest());

        // Φέρνουμε τους trainers για το dropdown list
        model.addAttribute("trainers", trainerService.findAllTrainers());

        return "appointment-booking"; // HTML στο templates/appointment-booking.html
    }

    // 2. Χειρισμός υποβολής φόρμας
    @PostMapping("/new")
    public String processAppointment(
            @Valid @ModelAttribute("appointmentRequest") CreateAppointmentRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        // Α) Έλεγχος validation
        if (bindingResult.hasErrors()) {
            model.addAttribute("trainers", trainerService.findAllTrainers());
            return "appointment-booking";
        }

        // Β) Εκτέλεση λογικής δημιουργίας ραντεβού
        CreateAppointmentResult result = appointmentService.createAppointment(request, true);

        if (!result.created()) {
            model.addAttribute("errorMessage", result.reason());
            model.addAttribute("trainers", trainerService.findAllTrainers());
            return "appointment-booking";
        }

        // Γ) Επιτυχία - Redirect στη λίστα των ραντεβού
        return "redirect:/appointments/my-appointments?success";
    }

    // 3. Προβολή λίστας ραντεβού χρήστη
    @GetMapping("/my-appointments")
    public String listAppointments(Model model) {
        // Αν θέλεις μπορείς να φορτώσεις τα ραντεβού του χρήστη εδώ
        // model.addAttribute("appointments", appointmentService.getUserAppointments(userId));
        return "appointment-list"; // HTML στο templates/appointment-list.html
    }
}
