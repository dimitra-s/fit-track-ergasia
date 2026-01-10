package gr.hua.dit.fittrack.web.controller;

import gr.hua.dit.fittrack.core.model.entity.ProgressRecord;
import gr.hua.dit.fittrack.core.model.entity.User;
import gr.hua.dit.fittrack.core.repository.UserRepository;
import gr.hua.dit.fittrack.core.service.ProgressService;
import gr.hua.dit.fittrack.core.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/profile/progress")
public class ProgressController {

    private final ProgressService progressService;
    private final UserService userService;

    public ProgressController(
            final ProgressService progressService, UserService userService) {
        if(progressService == null) throw new NullPointerException();
        if(userService ==  null) throw new NullPointerException();

        this.progressService = progressService;
        this.userService = userService;
    }

    // 1) Εμφάνιση της φόρμας (Get)
    @GetMapping
    public String showProgressForm(Authentication authentication, Model model) {
        String email = authentication.getName();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("Ο χρήστης δεν βρέθηκε"));

        model.addAttribute("user", user);
        return "add-progress";
    }

    // 2) Αποθήκευση της νέας μέτρησης (Post)
    @PostMapping
    public String addProgressRecord(Authentication authentication,
                                    @RequestParam Double currentWeight,
                                    @RequestParam Double runningTime,
                                    @RequestParam(required = false) String notes) {

        String email = authentication.getName();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("Ο χρήστης δεν βρέθηκε"));

        // 1. Δημιουργούμε νέα εγγραφή ιστορικού (ProgressRecord)
        ProgressRecord record = new ProgressRecord();
        record.setWeight(currentWeight);
        record.setRunningTime(runningTime);
        record.setDate(LocalDate.now()); // Χρησιμοποιούμε το LocalDate.now()
        record.setNotes(notes);
        record.setUser(user);

        // 2. Ενημερώνουμε τα "τρέχοντα" πεδία του User για το stats-box
        user.setCurrentWeight(currentWeight);
        user.setRunningTime(runningTime);

        // 3. Προσθέτουμε το record στη λίστα του χρήστη
        user.getProgressRecords().add(record);

        // 4. Αποθηκεύουμε τον χρήστη (λόγω CascadeType.ALL, θα σωθεί και το Record)
        //userRepository.save(user);

        return "redirect:/profile?success";
    }
}