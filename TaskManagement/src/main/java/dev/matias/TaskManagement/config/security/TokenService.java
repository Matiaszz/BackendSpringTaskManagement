package dev.matias.TaskManagement.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import dev.matias.TaskManagement.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user) {
        if (secret == null || secret.isBlank()) {
            throw new RuntimeException("Secret is not set! Check the environment variables.");
        }

        try {
            log.info("Generating token...");
            Algorithm algorithm = Algorithm.HMAC256(this.secret);

            return JWT.create()
                    .withSubject(user.getUsername())
                    .withClaim("id", user.getId().toString())
                    .withClaim("role", user.getRole().toString())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 86400000 * 2))
                    .sign(algorithm);

        } catch (Exception e) {
            log.error("Error generating token: {}", e.getMessage());
            return null;
        }
    }

    public String validateToken(String token) {
        try {
            log.info("Validating token...");
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (Exception e) {
            log.error("Error validating token: {}", e.getMessage());
            return null;
        }
    }

    public Map<String, Object> getClaims(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getClaims()
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().as(Object.class)));
        } catch (Exception e){
            log.error("Error extracting claims: {}", e.getMessage());
            return null;
        }
    }
}
