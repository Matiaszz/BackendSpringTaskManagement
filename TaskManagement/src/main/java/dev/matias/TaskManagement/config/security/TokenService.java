package dev.matias.TaskManagement.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import dev.matias.TaskManagement.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withSubject(user.getUsername()) // O token deve conter o nome do usuário
                    .withExpiresAt(new Date(System.currentTimeMillis() + 86400000)) // Expira em 24h
                    .sign(algorithm);
        } catch (Exception e) {
            System.out.println("Erro ao gerar token: " + e.getMessage());
            return null;
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (Exception e) {
            System.out.println("Erro ao validar token: " + e.getMessage());
            return null;
        }
    }
}
