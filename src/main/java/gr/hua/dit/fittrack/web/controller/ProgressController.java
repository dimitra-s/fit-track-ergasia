package gr.hua.dit.fittrack.web.controller;

import gr.hua.dit.fittrack.core.model.entity.ProgressRecord;
import gr.hua.dit.fittrack.core.model.entity.User;
import gr.hua.dit.fittrack.core.repository.UserRepository;
import gr.hua.dit.fittrack.core.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Controller
@RequestMapping("/progress/add")
public class ProgressController {

    private final UserService userService;
    private final UserRepository userRepository;

    public ProgressController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // Εμφάνιση της φόρμας
    @GetMapping
    public String showProgressForm(Authentication authentication, Model model) {
        if (authentication == null) return "redirect:/login";

        String email = authentication.getName();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user", user);
        return "add-progress";
    }

    // Αποθήκευση των δεδομένων
    @PostMapping
    @Transactional
    public String addProgressRecord(Authentication authentication,
                                    @RequestParam Double currentWeight,
                                    @RequestParam Double runningTime,
                                    @RequestParam(required = false) String notes) {

        if (authentication == null) return "redirect:/login";

        String email = authentication.getName();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Δημιουργία εγγραφής ιστορικού
        ProgressRecord record = new ProgressRecord();
        record.setWeight(currentWeight);
        record.setRunningTime(runningTime);
        record.setDate(LocalDate.now());
        record.setNotes(notes);
        record.setUser(user);

        // Ενημέρωση τρεχόντων στοιχείων χρήστη
        user.setCurrentWeight(currentWeight);
        user.setRunningTime(runningTime);
        user.getProgressRecords().add(record);

        // Αποθήκευση στη βάση
        userRepository.save(user);

        return "redirect:/profile?success";
    }
}