package gr.hua.dit.fittrack.web.controller;

import gr.hua.dit.fittrack.core.model.entity.Role;
import gr.hua.dit.fittrack.core.service.AuthService;
import gr.hua.dit.fittrack.core.service.impl.dto.RegisterUserRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthViewController {

    private final AuthService authService;

    public AuthViewController(final AuthService authService) {
        if(authService == null) throw new NullPointerException();

        this.authService = authService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // --- ΕΓΓΡΑΦΗ ΑΠΛΟΥ ΧΡΗΣΤΗ ---
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registerRequest",
                new RegisterUserRequest(
                        "",     // email
                        "",     // password
                        "",     // firstName
                        "",     // lastName
                        "",     // fitnessGoal
                        Role.USER,
                        "",     // specialization
                        ""      // area
                )
        );
        return "register";
    }


    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registerRequest") RegisterUserRequest request,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }
        try {
            authService.registerUser(request);
            return "redirect:/login?success";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }
}