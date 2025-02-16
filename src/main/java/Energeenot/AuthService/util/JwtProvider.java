package Energeenot.AuthService.util;

import Energeenot.AuthService.models.enums.Role;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${jwt.refreshExpiration}")
    private long refreshExpiration;

    public String generateToken(String email, Role role) {
        log.info("Generating JWT, email: {}, role: {}", email, role);
        return JWT.create()
                .withSubject(email)
                .withClaim("role", role.name())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .sign(Algorithm.HMAC256(secret));
    }
    public String generateRefreshToken(String email) {
        log.info("Generating refresh JWT, email: {}", email);
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshExpiration))
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateToken(String token) {
        log.info("Validating JWT, token: {}", token);
        return JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token)
                .getSubject();
    }
}
