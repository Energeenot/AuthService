package Energeenot.AuthService.service;

import Energeenot.AuthService.dto.AuthRequest;
import Energeenot.AuthService.dto.RegistrationResponse;
import Energeenot.AuthService.dto.TokenResponse;
import Energeenot.AuthService.exception.InvalidRefreshTokenException;
import Energeenot.AuthService.exception.UserAlreadyExistsException;
import Energeenot.AuthService.exception.UserNotFoundException;
import Energeenot.AuthService.models.User;
import Energeenot.AuthService.models.enums.Role;
import Energeenot.AuthService.repository.UserRepository;
import Energeenot.AuthService.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
                .orElseThrow(() -> new UserNotFoundException(String.format("User with with email: '%s' not found",
                        authRequest.getEmail())));

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new WrongThreadException("Wrong password");
        }

        String accessToken = jwtProvider.generateToken(authRequest.getEmail(), user.getRole(), user.getUuid());
        String refreshToken = jwtProvider.generateRefreshToken(authRequest.getEmail());

        user.setRefreshToken(passwordEncoder.encode(refreshToken));
        userRepository.save(user);

        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse refresh(String refreshToken) {
        String email = jwtProvider.validateToken(refreshToken);
        log.info("refreshing token for user with email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with email: '%s' not found",
                email)));

        if (!passwordEncoder.matches(refreshToken, user.getRefreshToken())){
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }

        String accessToken = jwtProvider.generateToken(email, user.getRole(), user.getUuid());
        String newRefreshToken = jwtProvider.generateRefreshToken(email);

        user.setRefreshToken(passwordEncoder.encode(refreshToken));
        userRepository.save(user);
        return new TokenResponse(accessToken, newRefreshToken);
    }

    public RegistrationResponse registration(AuthRequest authRequest) {
        log.info("Registering user with email: {}", authRequest.getEmail());
        if (userRepository.findByEmail(authRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(String.format("User with email: '%s' already exists",
                    authRequest.getEmail()));
        }
        User user = new User();
        String uuid  = UUID.randomUUID().toString();
        user.setUuid(uuid);
        user.setEmail(authRequest.getEmail());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        user.setRole(Role.ROLE_STUDENT);
        userRepository.save(user);
        return new RegistrationResponse(uuid, user.getRole().toString());
    }

}
