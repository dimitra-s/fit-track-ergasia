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
public class AppointmentViewController {

    private final AppointmentService appointmentService;
    private final TrainerRepository trainerRepository;

    public AppointmentViewController(AppointmentService appointmentService, TrainerRepository trainerRepository) {
        this.appointmentService = appointmentService;
        this.trainerRepository = trainerRepository;
    }

    // 1. Εμφάνιση Φόρμας
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        // Προσθέτουμε ένα άδειο request object για το th:object της Thymeleaf
        model.addAttribute("appointmentRequest", new CreateAppointmentRequest(null, null, null, ""));
        // Φέρνουμε τους trainers για το dropdown list [cite: 184]
        model.addAttribute("trainers", trainerRepository.findAll());
        return "appointments/create-appointment";
    }

    // 2. Χειρισμός Υποβολής & Κανόνων [cite: 195, 197, 198]
    @PostMapping("/new")
    public String processAppointment(
            @Valid @ModelAttribute("appointmentRequest") CreateAppointmentRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        // Α) Validation UI: Έλεγχος @Future, @NotNull από το record [cite: 21]
        if (bindingResult.hasErrors()) {
            model.addAttribute("trainers", trainerRepository.findAll());
            return "appointments/create-appointment";
        }

        // Β) Εκτέλεση Logic & Κανόνων (Past dates, Availability, Busy Trainer) [cite: 197, 198]
        CreateAppointmentResult result = appointmentService.createAppointment(request);

        if (!result.created()) {
            // Αν αποτύχει ένας κανόνας, στέλνουμε το reason στο UI [cite: 8]
            model.addAttribute("errorMessage", result.reason());
            model.addAttribute("trainers", trainerRepository.findAll());
            return "appointments/create-appointment";
        }

        // Γ) Επιτυχία
        return "redirect:/appointments/my-appointments?success";
    }

    // 3. Προβολή Ιστορικού/Λίστας [cite: 162]
    @GetMapping("/my-appointments")
    public String listAppointments(Model model) {
        // Εδώ θα καλέσεις την υπηρεσία για να φέρεις τα ραντεβού του χρήστη
        return "appointments/list";
    }
}