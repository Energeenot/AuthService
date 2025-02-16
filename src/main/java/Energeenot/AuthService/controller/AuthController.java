package Energeenot.AuthService.controller;

import Energeenot.AuthService.dto.AuthRequest;
import Energeenot.AuthService.dto.TokenResponse;
import Energeenot.AuthService.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        log.info("Login request with email: {}", authRequest.getEmail());
        return ResponseEntity.ok(authService.login(authRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody String refreshToken) {
        log.info("Refresh request with refresh token: {}", refreshToken);
        return ResponseEntity.ok(authService.refresh(refreshToken));
    }

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@Valid @RequestBody AuthRequest authRequest) {
        log.info("Registration request with email: {}", authRequest.getEmail());
        String message = authService.registration(authRequest);
        return ResponseEntity.ok(message);
    }
}
