package gr.hua.dit.fittrack.web;

import gr.hua.dit.fittrack.core.service.AppointmentService;
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
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final TrainerRepository trainerRepository;

    public AppointmentController(AppointmentService appointmentService, TrainerRepository trainerRepository) {
        this.appointmentService = appointmentService;
        this.trainerRepository = trainerRepository;
    }

    // Φόρμα για νέο ραντεβού
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        // Προετοιμασία του DTO για τη φόρμα
        model.addAttribute("appointmentRequest", new CreateAppointmentRequest(null, null, null, ""));
        // Λίστα trainers για το dropdown
        model.addAttribute("trainers", trainerRepository.findAll());
        return "appointments/create"; // appointments/create.html
    }

    // Επεξεργασία της φόρμας και εφαρμογή των κανόνων
    @PostMapping("/create")
    public String processCreate(
            @Valid @ModelAttribute("appointmentRequest") CreateAppointmentRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        // 1. Validation UI (π.χ. αν λείπουν πεδία ή η ημερομηνία δεν είναι @Future)
        if (bindingResult.hasErrors()) {
            model.addAttribute("trainers", trainerRepository.findAll());
            return "appointments/create";
        }

        // 2. Κλήση Service και έλεγχος των 3 κανόνων (Past date, Availability, Busy)
        CreateAppointmentResult result = appointmentService.createAppointment(request);

        if (!result.created()) {
            // Αν αποτύχει στέλνουμε το μήνυμα λάθους
            model.addAttribute("errorMessage", result.reason());
            model.addAttribute("trainers", trainerRepository.findAll());
            return "appointments/appointment-booking";
        }

        // 3. Επιτυχία - Ανακατεύθυνση στη λίστα
        return "redirect:/appointments/my-appointments?success";
    }

    @GetMapping("/my-appointments")
    public String listAppointments(Model model) {
        // ραντεβού χρήστη
        return "appointments/appointment-list";
    }
}