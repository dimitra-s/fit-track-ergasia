package gr.hua.dit.fittrack.web.controller;

import gr.hua.dit.fittrack.core.model.entity.User;
import gr.hua.dit.fittrack.core.model.entity.Trainer;
import gr.hua.dit.fittrack.core.repository.UserRepository;
import gr.hua.dit.fittrack.core.repository.TrainerRepository;
import gr.hua.dit.fittrack.core.service.TrainerService;
import gr.hua.dit.fittrack.core.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProfileController {

    private final UserService userService;
    private final TrainerService trainerService;

    public ProfileController(final UserService userService,
                             final TrainerService trainerService) {
        if(userService == null) throw new NullPointerException();
        if(trainerService == null) throw new NullPointerException();

        this.userService = userService;
        this.trainerService = trainerService;
    }

    // Εμφάνιση Προφίλ Χρήστη
    @GetMapping("/profile")
    public String showProfile(Authentication authentication, Model model) {
        String email = authentication.getName();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("Ο χρήστης δεν βρέθηκε"));

        model.addAttribute("user", user);
        return "profile";
    }

    // Εμφάνιση Προφίλ Trainer
    @GetMapping("/trainers/{id}")
    public String showTrainerProfile(@PathVariable Long id, Model model) {
        Trainer trainer = trainerService.findTrainerById(id)
                .orElseThrow(() -> new RuntimeException("Ο Trainer δεν βρέθηκε"));

        model.addAttribute("trainer", trainer);
        return "trainer-profile";
    }
}