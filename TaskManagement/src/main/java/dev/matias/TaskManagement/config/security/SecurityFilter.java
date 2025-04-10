package dev.matias.TaskManagement.config.security;

import dev.matias.TaskManagement.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Component
@Slf4j
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService;

    @Autowired
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = this.recoverToken(request);
        log.info("Token: {}", token);

        if (token != null){
            String login = tokenService.validateToken(token);

            UserDetails user = userRepository.findByUsername(login).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found (Security Filter)"));

            var authentication = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities());
            log.info("Successful authentication for user: {}", user.getUsername());

            // Save user in context
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {

        if (request.getCookies() != null){
            for (var cookie : request.getCookies()){
                if ("token".equalsIgnoreCase(cookie.getName())){
                    log.info("Token received from cookie: {}", cookie.getName());
                    return cookie.getValue();
                }
            }
        }
        log.info("Token in token recovery is null.");
        return null;
    }
}
