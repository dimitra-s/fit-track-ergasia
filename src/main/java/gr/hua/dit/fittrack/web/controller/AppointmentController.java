package gr.hua.dit.fittrack.web.controller;

import gr.hua.dit.fittrack.core.model.entity.Appointment;
import gr.hua.dit.fittrack.core.model.entity.AppointmentType;
import gr.hua.dit.fittrack.core.model.entity.Trainer;
import gr.hua.dit.fittrack.core.model.entity.User;
import gr.hua.dit.fittrack.core.repository.TrainerRepository;
import gr.hua.dit.fittrack.core.repository.UserRepository;
import gr.hua.dit.fittrack.core.service.AppointmentService;
import gr.hua.dit.fittrack.core.service.TrainerService;
import gr.hua.dit.fittrack.core.service.UserService;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAppointmentRequest;
import gr.hua.dit.fittrack.core.service.impl.dto.CreateAppointmentResult;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final TrainerService trainerService;
    private final UserService userService;
    private final TrainerRepository trainerRepository;


    public AppointmentController(final AppointmentService appointmentService,
                                 final TrainerService trainerService,
                                 final UserService userService,final TrainerRepository trainerRepository
) {
        if (appointmentService == null) throw new NullPointerException();
        if (trainerService == null) throw new NullPointerException();
        if(userService == null) throw new NullPointerException();

        this.appointmentService = appointmentService;
        this.trainerService = trainerService;
        this.userService = userService;
        this.trainerRepository = trainerRepository;
    }

    // ------------------------
    // Προβολή φόρμας δημιουργίας (GET)
    // ------------------------
//    @GetMapping("/create")
//    public String showCreateForm(@RequestParam("trainerId") Long trainerId, Model model, Authentication authentication) {
//        // 1. Βρίσκουμε τον συνδεδεμένο χρήστη από το email του
//        String email = authentication.getName();
//        User currentUser = userService.getUserByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // 2. Βρίσκουμε τον Trainer για τον οποίο γίνεται η κράτηση
//        Trainer trainer = trainerService.findTrainerById(trainerId)
//                .orElseThrow(() -> new RuntimeException("Trainer not found"));
//
//        // 3. Αρχικοποιούμε το Request με τα ID του χρήστη και του trainer
//        // Αυτό διασφαλίζει ότι τα hidden fields στη φόρμα θα έχουν τιμές
//        CreateAppointmentRequest request = new CreateAppointmentRequest(
//                currentUser.getId(),
//                trainerId,
//                null, // Το dateTime θα επιλεγεί από το dropdown
//                AppointmentType.INDOOR,
//                ""
//        );
//
//        // 4. Φέρνουμε τα διαθέσιμα slots από το Service
//        List<LocalDateTime> availableSlots = appointmentService.getAvailableSlots(trainerId);
//
//        // 5. Προσθήκη στο Model για το Thymeleaf
//        model.addAttribute("appointmentRequest", request);
//        model.addAttribute("trainer", trainer);
//        model.addAttribute("availableSlots", availableSlots);
//
//        return "create-appointment";
//    }

    @GetMapping("/create")
    public String showCreateForm(@RequestParam(value = "trainerId", required = false) Long trainerId,
                                 Model model,
                                 Authentication authentication) {

        String email = authentication.getName();
        User currentUser = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Trainer> trainers = trainerRepository.findAll();

        Trainer trainer = null;
        List<LocalDateTime> availableSlots = List.of();

        if (trainerId != null) {
            trainer = trainerService.findTrainerById(trainerId)
                    .orElseThrow(() -> new RuntimeException("Trainer not found"));
            availableSlots = appointmentService.getAvailableSlots(trainerId);
        }

        CreateAppointmentRequest request = new CreateAppointmentRequest(
                currentUser.getId(),
                trainerId,
                null,
                AppointmentType.INDOOR,
                ""
        );

        model.addAttribute("appointmentRequest", request);
        model.addAttribute("trainers", trainers);
        model.addAttribute("trainer", trainer);
        model.addAttribute("availableSlots", availableSlots);

        return "create-appointment";
    }


    // ------------------------
    // Υποβολή φόρμας (POST)
    // ------------------------
    @PostMapping("/create")
    public String processCreate(@Valid @ModelAttribute("appointmentRequest") CreateAppointmentRequest request,
                                BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

        // Αν υπάρχουν σφάλματα επικύρωσης (π.χ. κενά πεδία)
        if (bindingResult.hasErrors()) {
            model.addAttribute("trainer", trainerService.findTrainerById(request.trainerId()).orElse(null));
            model.addAttribute("availableSlots", appointmentService.getAvailableSlots(request.trainerId()));
            return "create-appointment";
        }

        // Κλήση του Service για δημιουργία του ραντεβού
        CreateAppointmentResult result = appointmentService.createAppointment(request, false);

        // Αν το Service επιστρέψει αποτυχία (π.χ. η ώρα κλείστηκε ενδιάμεσα)
        if (!result.created()) {
            model.addAttribute("errorMessage", result.reason());
            model.addAttribute("trainer", trainerService.findTrainerById(request.trainerId()).orElse(null));
            model.addAttribute("availableSlots", appointmentService.getAvailableSlots(request.trainerId()));
            return "create-appointment";
        }

        // Επιτυχία: Ανακατεύθυνση στο προφίλ με μήνυμα
        redirectAttributes.addFlashAttribute("success", "Το ραντεβού σας καταχωρήθηκε επιτυχώς!");
        return "redirect:/profile";
    }

    @PostMapping("/cancel/{id}")
    public String cancel(@PathVariable Long id,
                         RedirectAttributes redirectAttributes) {

        appointmentService.cancelAppointment(id); // ή όπως το έχεις

        redirectAttributes.addFlashAttribute("success", "Το ραντεβού ακυρώθηκε.");
        return "redirect:/appointments/my-appointments";
    }

    @GetMapping("/{id}/notes")
    public String showNotes(@PathVariable Long id, Model model) {

        Appointment appointment = appointmentService.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        model.addAttribute("appointment", appointment);
        return "appointment-notes";
    }

    @PostMapping("/{id}/notes")
    public String saveNotes(@PathVariable Long id,
                            @RequestParam("notes") String notes,
                            RedirectAttributes redirectAttributes) {

        appointmentService.updateNotes(id, notes);

        redirectAttributes.addFlashAttribute("success",
                "Οι σημειώσεις αποθηκεύτηκαν επιτυχώς");
        return "redirect:/appointments/my-appointments";
    }



}