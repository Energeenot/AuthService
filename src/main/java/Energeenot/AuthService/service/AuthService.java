package Energeenot.AuthService.service;

import Energeenot.AuthService.dto.AuthRequest;
import Energeenot.AuthService.dto.TokenResponse;
import Energeenot.AuthService.exception.UserAlreadyExistsException;
import Energeenot.AuthService.models.User;
import Energeenot.AuthService.models.enums.Role;
import Energeenot.AuthService.repository.UserRepository;
import Energeenot.AuthService.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public TokenResponse login(AuthRequest authRequest) {
        log.info("try to login with email: {}", authRequest.getEmail());
        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        String accessToken = jwtProvider.generateToken(authRequest.getEmail(), user.getRole());
        String refreshToken = jwtProvider.generateRefreshToken(authRequest.getEmail());

        user.setRefreshToken(passwordEncoder.encode(refreshToken));
        userRepository.save(user);

        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse refresh(String refreshToken) {
        String email = jwtProvider.validateToken(refreshToken);
        log.info("refreshing token for user with email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(refreshToken, user.getRefreshToken())){
            throw new RuntimeException("Invalid refresh token");
        }

        String accessToken = jwtProvider.generateToken(email, user.getRole());
        String newRefreshToken = jwtProvider.generateRefreshToken(email);

        user.setRefreshToken(passwordEncoder.encode(refreshToken));
        userRepository.save(user);

        return new TokenResponse(accessToken, newRefreshToken);
    }

    public String registration(AuthRequest authRequest) {
        log.info("Registering user with email: {}", authRequest.getEmail());
        if (userRepository.findByEmail(authRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(String.format("User with email: '%s' already exists",
                    authRequest.getEmail()));
        }
        User user = new User();
        user.setEmail(authRequest.getEmail());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        user.setRole(Role.ROLE_STUDENT);
        userRepository.save(user);
        return "Registered successfully";
    }

}
