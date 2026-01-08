package gr.hua.dit.fittrack.web;

import gr.hua.dit.fittrack.core.model.entity.User;
import gr.hua.dit.fittrack.core.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProfileController {

    private final UserRepository userRepository;

    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    public String showProfile(Authentication authentication, Model model) {
        // Η Spring Security ξέρει ποιος συνδέθηκε (το email του)
        String email = authentication.getName();

        // Τον βρίσκουμε στη βάση δεδομένων
        User user = userRepository.findByEmailAddress(email)
                .orElseThrow(() -> new RuntimeException("Ο χρήστης δεν βρέθηκε"));

        // Περνάμε το αντικείμενο user στην HTML
        model.addAttribute("user", user);

        return "profile"; // Θα ανοίξει το profile.html

    }

    @GetMapping("/trainer/{id}")
    public String showTrainerProfile(@PathVariable Long id, Model model) {
        // Ψάχνουμε τον Trainer στη βάση με βάση το ID
        User trainer = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ο Trainer δεν βρέθηκε"));

        // Σιγουρευόμαστε ότι είναι όντως Trainer
        if (!trainer.getRole().name().equals("TRAINER")) {
            return "redirect:/"; // Αν δεν είναι trainer, γύρνα στην αρχική
        }

        model.addAttribute("trainer", trainer);
        return "trainer-profile"; // Θα φτιάξουμε αυτό το HTML
    }
}