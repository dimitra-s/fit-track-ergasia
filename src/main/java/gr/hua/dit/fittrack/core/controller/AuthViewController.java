package gr.hua.dit.fittrack.core.controller; // Προτείνω να μπει στο web package

import gr.hua.dit.fittrack.core.service.AuthService;
import gr.hua.dit.fittrack.core.service.impl.dto.RegisterUserRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthViewController {

    private final AuthService authService;

    public AuthViewController(AuthService authService) {
        this.authService = authService;
    }

    // Εμφάνιση Φόρμας Login
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // ΣΗΜΑΝΤΙΚΟ: To POST /login ΔΕΝ το γράφουμε εμείς,
    // το διαχειρίζεται το Spring Security αυτόματα!

    // Εμφάνιση Φόρμας Εγγραφής
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String submitRegisterForm(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password,
            Model model
    ) {
        try {
            // Δημιουργία του Request DTO με βάση τα πεδία που ορίσαμε
            RegisterUserRequest req = new RegisterUserRequest(
                    email,
                    password,
                    firstName,
                    lastName
            );

            authService.registerUser(req);

            // Μετά την εγγραφή, στέλνουμε τον χρήστη στο login με μήνυμα επιτυχίας
            return "redirect:/login?registered=true";

        } catch (Exception e) {
            model.addAttribute("error", "Η εγγραφή απέτυχε: " + e.getMessage());
            return "register";
        }
    }
}