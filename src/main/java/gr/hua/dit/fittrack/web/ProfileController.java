package gr.hua.dit.fittrack.web;

import gr.hua.dit.fittrack.core.model.entity.User;
import gr.hua.dit.fittrack.core.model.entity.Trainer;
import gr.hua.dit.fittrack.core.repository.UserRepository;
import gr.hua.dit.fittrack.core.repository.TrainerRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProfileController {

    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository; // Προσθήκη repository

    public ProfileController(UserRepository userRepository, TrainerRepository trainerRepository) {
        this.userRepository = userRepository;
        this.trainerRepository = trainerRepository;
    }

    @GetMapping("/profile")
    public String showProfile(Authentication authentication, Model model) {
        String email = authentication.getName();
        User user = userRepository.findByEmailAddress(email)
                .orElseThrow(() -> new RuntimeException("Ο χρήστης δεν βρέθηκε"));
        model.addAttribute("user", user);
        return "profile";
    }

    // Το αλλάζουμε σε /trainers/{id} για να ταιριάζει με το link που συνήθως έχουμε
    @GetMapping("/trainers/{id}")
    public String showTrainerProfile(@PathVariable Long id, Model model) {
        // Ψάχνουμε πλέον στο trainerRepository
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ο Trainer δεν βρέθηκε"));

        model.addAttribute("trainer", trainer);
        return "trainer-profile";
    }
}