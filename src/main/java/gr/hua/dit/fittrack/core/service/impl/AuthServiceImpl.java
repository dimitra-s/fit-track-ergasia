package gr.hua.dit.fittrack.core.service.impl;

import gr.hua.dit.fittrack.core.model.entity.Role;
import gr.hua.dit.fittrack.core.model.entity.User;
import gr.hua.dit.fittrack.core.repository.UserRepository;
import gr.hua.dit.fittrack.core.security.JwtService;
import gr.hua.dit.fittrack.core.service.AuthService;
import gr.hua.dit.fittrack.core.service.impl.dto.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public void registerUser(RegisterUserRequest request) {
        if (userRepository.findByEmailAddress(request.email()).isPresent()) {
            throw new RuntimeException("Το email χρησιμοποιείται ήδη.");
        }

        User user = new User();
        user.setUsername(request.email());
        user.setEmailAddress(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setUserFirstName(request.firstName());
        user.setUserLastName(request.lastName());
        user.setFitnessGoal(request.fitnessGoal());
        user.setRole(Role.USER);

        userRepository.save(user);
    }

    @Override
    public LoginResult login(LoginRequest request) {
        return userRepository.findByEmailAddress(request.email())
                .filter(user -> passwordEncoder.matches(request.password(), user.getPassword()))
                .map(user -> {
                    String token = jwtService.issue(user.getEmailAddress(), user.getRole().name());

                    return LoginResult.success(
                            token,
                            3600,
                            user.getRole().name(),
                            user.getId()
                    );
                })
                .orElseGet(() -> LoginResult.fail("Λάθος email ή κωδικός πρόσβασης"));
    }
}