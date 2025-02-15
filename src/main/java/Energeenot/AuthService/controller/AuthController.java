package Energeenot.AuthService.controller;

import Energeenot.AuthService.dto.AuthRequest;
import Energeenot.AuthService.exception.UserAlreadyExistsException;
import Energeenot.AuthService.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody AuthRequest authRequest) {
        log.info("Login request with email: {}", authRequest.getEmail());
        String token = authService.login(authRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@Valid @RequestBody AuthRequest authRequest) {
        log.info("Registration request with email: {}", authRequest.getEmail());
        String message = authService.registration(authRequest);
        return ResponseEntity.ok(message);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExists(UserAlreadyExistsException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
