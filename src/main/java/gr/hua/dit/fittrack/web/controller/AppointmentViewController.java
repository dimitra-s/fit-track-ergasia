package gr.hua.dit.fittrack.web.controller;

import gr.hua.dit.fittrack.core.model.entity.Appointment;
import gr.hua.dit.fittrack.core.model.entity.AppointmentType;
import gr.hua.dit.fittrack.core.model.entity.Trainer;
import gr.hua.dit.fittrack.core.repository.TrainerRepository;
import gr.hua.dit.fittrack.core.service.AppointmentService;
import gr.hua.dit.fittrack.core.service.TrainerService;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAppointmentRequest;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAppointmentResult;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import gr.hua.dit.fittrack.core.repository.UserRepository;


import java.util.List;

@Controller
@RequestMapping("/appointments")
public class AppointmentViewController {

    private final AppointmentService appointmentService;
    private final TrainerService trainerService;
    private final TrainerRepository trainerRepository; // Προσθήκη
    private final UserRepository userRepository;

    public AppointmentViewController(
            final AppointmentService appointmentService,
            final TrainerService trainerService,
            final TrainerRepository trainerRepository,
            UserRepository userRepository) {
        this.appointmentService = appointmentService;
        this.trainerService = trainerService;
        this.trainerRepository = trainerRepository;
        this.userRepository = userRepository;
    }

//    @GetMapping("/new")
//    public String showCreateForm(Model model) {
//        model.addAttribute("appointmentRequest", new CreateAppointmentRequest());
//        model.addAttribute("trainers", trainerService.findAllTrainers());
//        return "appointment-booking";
//    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        System.out.println("HIT GET /appointments/new");

        model.addAttribute("appointmentRequest",
                new CreateAppointmentRequest(
                        0L,                 // προσωρινό - θα αντικατασταθεί στο POST
                        0L,
                        null,
                        AppointmentType.INDOOR,
                        ""
                )
        );
        model.addAttribute("trainers", trainerService.findAllTrainers());
        return "appointment-booking";
    }
    @PostMapping("/new")
    public String processAppointment(
            @ModelAttribute("appointmentRequest") CreateAppointmentRequest request,
            BindingResult bindingResult,
            Model model,
            Authentication authentication
    ) {
        // 1) Βρες userId από login
        String email = authentication.getName();
        Long userId = userRepository.findByEmailAddress(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();

        // 2) Φτιάξε το fixed request (με σωστό userId)
        CreateAppointmentRequest fixed = new CreateAppointmentRequest(
                userId,
                request.trainerId(),
                request.dateTime(),
                request.type(),
                request.notes()
        );

        // 3) ΚΑΝΕ validation πάνω στο fixed (προαιρετικά, αλλά καλό)
        // πιο απλά: έλεγξε βασικά nulls εδώ:
        if (fixed.trainerId() == null || fixed.dateTime() == null || fixed.type() == null) {
            model.addAttribute("errorMessage", "Συμπλήρωσε όλα τα πεδία.");
            model.addAttribute("trainers", trainerService.findAllTrainers());
            return "appointment-booking";
        }

        CreateAppointmentResult result = appointmentService.createAppointment(fixed, true);

        if (!result.created()) {
            model.addAttribute("errorMessage", result.reason());
            model.addAttribute("trainers", trainerService.findAllTrainers());
            return "appointment-booking";
        }

        return "redirect:/appointments/my-appointments?success";
    }

  //  @PostMapping("/new")
//    public String processAppointment(
//            @Valid @ModelAttribute("appointmentRequest") CreateAppointmentRequest request,
//            BindingResult bindingResult,
//            Model model,
//            Authentication authentication
//    ) {
//
//            if (bindingResult.hasErrors()) {
//                System.out.println("BINDING ERRORS:");
//                bindingResult.getFieldErrors().forEach(err ->
//                        System.out.println(" - field=" + err.getField()
//                                + " rejected=" + err.getRejectedValue()
//                                + " msg=" + err.getDefaultMessage())
//                );
//
//                model.addAttribute("trainers", trainerService.findAllTrainers());
//                return "appointment-booking";
//            }
//
//
//
//        String email = authentication.getName();
//        Long userId = userRepository.findByEmailAddress(email)
//                .orElseThrow(() -> new RuntimeException("User not found"))
//                .getId();
//
//        CreateAppointmentRequest fixed = new CreateAppointmentRequest(
//                userId,
//                request.trainerId(),
//                request.dateTime(),
//                request.type(),
//                request.notes()
//        );
//        System.out.println("SUBMIT appointment: trainerId=" + request.trainerId()
//                + ", dateTime=" + request.dateTime()
//                + ", type=" + request.type());
//
//        CreateAppointmentResult result = appointmentService.createAppointment(fixed, true);
//
//        System.out.println("RESULT created=" + result.created() + " reason=" + result.reason());
//
//
//        if (!result.created()) {
//            model.addAttribute("errorMessage", result.reason());
//            model.addAttribute("trainers", trainerService.findAllTrainers());
//            return "appointment-booking";
//        }
//
//        return "redirect:/appointments/my-appointments?success";
//    }


    // Η ΜΕΓΑΛΗ ΑΛΛΑΓΗ ΕΔΩ
    @GetMapping("/my-appointments")
    public String listAppointments(Authentication authentication, Model model) {
        String email = authentication.getName();

        // Ελέγχουμε αν ο χρήστης έχει ρόλο TRAINER
        boolean isTrainer = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_TRAINER"));

        if (isTrainer) {
            // 1. Φέρνουμε τα ραντεβού που αφορούν τον Trainer
            List<Appointment> trainerApps = appointmentService.getAppointmentsByTrainer(email);
            model.addAttribute("appointments", trainerApps);

            // 2. Φέρνουμε τα στοιχεία του Trainer για το header (όνομα κλπ)
            Trainer trainer = trainerRepository.findByEmail(email).orElse(null);
            model.addAttribute("trainer", trainer);

            // 3. ΕΠΙΣΤΡΕΦΟΥΜΕ ΤΟ ΣΩΣΤΟ HTML (Αυτό με το "+ Ορισμός Ωραρίου")
            return "trainer-availability";
        }

        // Αλλιώς (αν είναι USER), επιστρέφουμε την κλασική λίστα
        model.addAttribute("appointments", appointmentService.getAppointmentsByUser(email));
        return "appointment-list";
    }

    @PostMapping("/update-status") // Διόρθωση path (αφαιρέθηκε το διπλό /appointments)
    public String updateAppointmentStatus(@RequestParam Long id, @RequestParam String status) {
        appointmentService.updateStatus(id, status);
        return "redirect:/appointments/my-appointments?statusUpdated";
    }

    @GetMapping("/notes/{id}") // Διόρθωση path
    public String showNotesPage(@PathVariable Long id, Model model) {
        Appointment app = appointmentService.findById(id);
        model.addAttribute("appointment", app);
        return "appointments-notes";
    }

    @PostMapping("/notes/save") // Διόρθωση path
    public String saveAppointmentNotes(@RequestParam Long id, @RequestParam String notes) {
        appointmentService.updateNotes(id, notes);
        return "redirect:/appointments/my-appointments?notesSaved";
    }
}