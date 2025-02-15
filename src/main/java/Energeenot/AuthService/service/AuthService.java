package Energeenot.AuthService.service;

import Energeenot.AuthService.dto.AuthRequest;
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

    public String login(AuthRequest authRequest) {
        log.info("try to login with email: {}", authRequest.getEmail());
        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        return jwtProvider.generateToken(authRequest.getEmail(), user.getRole());
    }

    public String registration(AuthRequest authRequest) {
        log.info("Registering user with email: {}", authRequest.getEmail());
        if (userRepository.findByEmail(authRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(String.format("User with email: '%s' already exists", authRequest.getEmail()));
        }
        User user = new User();
        user.setEmail(authRequest.getEmail());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        user.setRole(Role.ROLE_STUDENT);
        userRepository.save(user);
        return "Registered successfully";
    }

}
