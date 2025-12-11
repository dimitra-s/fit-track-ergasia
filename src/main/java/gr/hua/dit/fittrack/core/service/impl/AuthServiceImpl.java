package gr.hua.dit.fittrack.core.service.impl;

import gr.hua.dit.fittrack.core.model.entity.Role;
import gr.hua.dit.fittrack.core.model.entity.User;
import gr.hua.dit.fittrack.core.repository.UserRepository;
import gr.hua.dit.fittrack.core.security.JwtService;
import gr.hua.dit.fittrack.core.service.AuthService;
import gr.hua.dit.fittrack.core.service.mapper.UserMapper;
import gr.hua.dit.fittrack.core.service.model.*;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final Validator validator;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    public AuthServiceImpl(Validator validator,
                           PasswordEncoder passwordEncoder,
                           UserRepository userRepository,
                           UserMapper userMapper,
                           JwtService jwtService) {
        if (validator == null) throw new NullPointerException();
        if (passwordEncoder == null) throw new NullPointerException();
        if (userRepository == null) throw new NullPointerException();
        if (userMapper == null) throw new NullPointerException();
        if (jwtService == null) throw new NullPointerException();

        this.validator = validator;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
    }

    // ------------------------
    // 1. REGISTER USER
    // ------------------------
    @Transactional
    @Override
    public RegisterUserResult registerUser(RegisterUserRequest request) {
        if (request == null) {
            throw new NullPointerException();
        }

        // Validate DTO (όπως στο OfficeHours)
        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<RegisterUserRequest> v : violations) {
                sb.append(v.getPropertyPath())
                        .append(": ")
                        .append(v.getMessage())
                        .append("\n");
            }
            return RegisterUserResult.fail(sb.toString());
        }

        String email = request.email().strip();
        String rawPassword = request.password().strip();

        // Uniqueness (email)
        if (userRepository.existsByEmailAddressIgnoreCase(email)) {
            return RegisterUserResult.fail("Email already registered");
        }

        // Encode password
        String hashedPassword = passwordEncoder.encode(rawPassword);

        // Δημιουργία User entity
        User user = new User();
        user.setEmailAddress(email);
        user.setPassword(hashedPassword);
        user.setUserFirstName(request.firstName().strip());
        user.setUserLastName(request.lastName().strip());
        user.setRole(Role.USER);

        // Προαιρετικό: validation στο entity (όπως ο καθηγητής)
        Set<ConstraintViolation<User>> userViolations = validator.validate(user);
        if (!userViolations.isEmpty()) {
            throw new RuntimeException("Invalid User entity");
        }

        user = userRepository.save(user);

        UserView view = userMapper.convertUserToUserView(user);

        return RegisterUserResult.success(view);
    }

    // ------------------------
    // 2. LOGIN
    // ------------------------
    @Override
    public LoginResult login(LoginRequest request) {
        if (request == null) {
            throw new NullPointerException();
        }

        // validate DTO
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<LoginRequest> v : violations) {
                sb.append(v.getPropertyPath())
                        .append(": ")
                        .append(v.getMessage())
                        .append("\n");
            }
            return LoginResult.fail(sb.toString());
        }

        String email = request.email().strip();
        String rawPassword = request.password().strip();

        // Φέρνουμε χρήστη με βάση το email
        User user = userRepository.findByEmailAddressIgnoreCase(email).orElse(null);
        if (user == null) {
            return LoginResult.fail("Invalid email or password");
        }

        // Έλεγχος password
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            return LoginResult.fail("Invalid email or password");
        }

        // Δημιουργία JWT token
        String token = jwtService.generateToken(
                user.getEmailAddress(),
                user.getRole().name()
        );

        return LoginResult.success(token);
    }
}
