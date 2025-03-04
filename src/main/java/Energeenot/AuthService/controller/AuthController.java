package Energeenot.AuthService.controller;

import Energeenot.AuthService.dto.AuthRequest;
import Energeenot.AuthService.dto.RegistrationResponse;
import Energeenot.AuthService.dto.TokenResponse;
import Energeenot.AuthService.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
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

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        log.info("Login request with email: {}", authRequest.getEmail());
        return ResponseEntity.ok(authService.login(authRequest));
    }

    @PostMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenResponse> refresh(@RequestBody String refreshToken) {
        log.info("Refresh request with refresh token: {}", refreshToken);
        return ResponseEntity.ok(authService.refresh(refreshToken));
    }

    @PostMapping(value = "/registration", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RegistrationResponse> registration(@Valid @RequestBody AuthRequest authRequest) {
        log.info("Registration request with email: {}", authRequest.getEmail());
        return ResponseEntity.ok(authService.registration(authRequest));
    }
}
