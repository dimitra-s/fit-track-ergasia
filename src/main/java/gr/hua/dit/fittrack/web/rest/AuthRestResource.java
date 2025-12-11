package gr.hua.dit.fittrack.web.rest;

import gr.hua.dit.fittrack.core.service.AuthService;
import gr.hua.dit.fittrack.core.service.impl.dto.LoginRequest;
import gr.hua.dit.fittrack.core.service.impl.dto.LoginResult;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * REST controller για authentication (JWT login).
 */
@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthRestResource {

    private final AuthService authService;

    public AuthRestResource(AuthService authService) {
        if (authService == null) throw new NullPointerException();
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResult> login(@RequestBody @Valid LoginRequest request) {

        LoginResult result = authService.login(request);

        if (!result.success()) {
            // 401 όπως κάνει ο καθηγητής
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, result.reason());
        }

        return ResponseEntity.ok(result);
    }
}
