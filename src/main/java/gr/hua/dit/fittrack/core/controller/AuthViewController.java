package gr.hua.dit.fittrack.core.controller;

import gr.hua.dit.fittrack.core.service.AuthService;
import gr.hua.dit.fittrack.core.service.impl.dto.LoginDto;
import gr.hua.dit.fittrack.core.service.impl.dto.RegisterDto;
import gr.hua.dit.fittrack.core.service.model.LoginRequest;
import gr.hua.dit.fittrack.core.service.model.RegisterUserRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthViewController {

    private final AuthService authService;

    public AuthViewController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String submitLoginForm(
            @RequestParam String username,
            @RequestParam String password,
            Model model
    ) {
        try {
            LoginDto dto = new LoginDto(username, password);

            LoginRequest request = new LoginRequest(dto.username(), dto.password());

            authService.login(request);

            return "redirect:/profile";

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String submitRegisterForm(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String role,
            Model model
    ) {
        try {

            RegisterDto dto = new RegisterDto(username, email, password, role);

            RegisterUserRequest req = new RegisterUserRequest(
                    dto.username(),
                    dto.email(),
                    dto.password(),
                    dto.role()
            );

            authService.registerUser(req);

            return "redirect:/login";

        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }
}
