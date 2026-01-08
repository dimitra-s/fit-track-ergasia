package gr.hua.dit.fittrack.web;

import gr.hua.dit.fittrack.core.repository.TrainerRepository; // Προσοχή στο σωστό πακέτο σου
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Αυτό λείπει για το Model
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/trainers")
public class TrainerViewController {

    private final TrainerRepository trainerRepository;

    // Το Spring θα κάνει αυτόματα inject το bean του Repository
    public TrainerViewController(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    @GetMapping
    public String showPublicTrainers(Model model) {
        // Τραβάμε όλους τους trainers για να τους δει ο Επισκέπτης [cite: 184]
        model.addAttribute("trainers", trainerRepository.findAll());
        return "trainers-list";
    }
}