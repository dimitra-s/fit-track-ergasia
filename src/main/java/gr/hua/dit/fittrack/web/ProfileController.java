package gr.hua.dit.fittrack.web;

import gr.hua.dit.fittrack.core.model.entity.User;
import gr.hua.dit.fittrack.core.model.entity.Trainer;
import gr.hua.dit.fittrack.core.repository.UserRepository;
import gr.hua.dit.fittrack.core.repository.TrainerRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProfileController {

    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;

    public ProfileController(UserRepository userRepository, TrainerRepository trainerRepository) {
        this.userRepository = userRepository;
        this.trainerRepository = trainerRepository;
    }

    // Εμφάνιση Προφίλ Χρήστη
    @GetMapping("/profile")
    public String showProfile(Authentication authentication, Model model) {
        String email = authentication.getName();
        User user = userRepository.findByEmailAddress(email)
                .orElseThrow(() -> new RuntimeException("Ο χρήστης δεν βρέθηκε"));
        model.addAttribute("user", user);
        return "profile";
    }

    // Φόρμα Ενημέρωσης Προόδου (Βάρος/Τρέξιμο)
    @GetMapping("/profile/progress")
    public String showProgressForm(Authentication authentication, Model model) {
        String email = authentication.getName();
        User user = userRepository.findByEmailAddress(email)
                .orElseThrow(() -> new RuntimeException("Ο χρήστης δεν βρέθηκε"));
        model.addAttribute("user", user);
        return "progress";
    }

    // Αποθήκευση Προόδου
    @PostMapping("/profile/progress")
    public String updateProgress(Authentication authentication,
                                 @RequestParam Double currentWeight,
                                 @RequestParam Double runningTime) {
        String email = authentication.getName();
        User user = userRepository.findByEmailAddress(email)
                .orElseThrow(() -> new RuntimeException("Ο χρήστης δεν βρέθηκε"));

        user.setCurrentWeight(currentWeight);
        user.setRunningTime(runningTime);

        userRepository.save(user);
        return "redirect:/profile?success";
    }

    // Εμφάνιση Προφίλ Trainer
    @GetMapping("/trainers/{id}")
    public String showTrainerProfile(@PathVariable Long id, Model model) {
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ο Trainer δεν βρέθηκε"));

        model.addAttribute("trainer", trainer);
        return "trainer-profile";
    }
}