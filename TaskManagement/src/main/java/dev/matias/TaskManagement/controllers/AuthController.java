package dev.matias.TaskManagement.controllers;

import dev.matias.TaskManagement.config.security.TokenService;
import dev.matias.TaskManagement.domain.User;
import dev.matias.TaskManagement.domain.UserRole;
import dev.matias.TaskManagement.dtos.AuthenticationDTO;
import dev.matias.TaskManagement.dtos.LoginResponseDTO;
import dev.matias.TaskManagement.dtos.RegisterDTO;
import dev.matias.TaskManagement.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder pwdEncoder;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        log.info("Authenticating user {}", data.username());
        var auth = authenticationManager.authenticate(usernamePassword);
        log.info("User {} authenticated", data.username());

        // getPrincipal = User object
        String token = tokenService.generateToken((User) auth.getPrincipal());

        ResponseCookie cookie = ResponseCookie.from("token", token).httpOnly(true).secure(false)
                .sameSite("Strict").path("/").maxAge(86400).build();

        LoginResponseDTO dto = new LoginResponseDTO(token, ((User) auth.getPrincipal()));
        return ResponseEntity.ok().header("Set-Cookie", cookie.toString()).body(dto);
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDTO> register(@RequestBody @Valid RegisterDTO data){
        if (this.userRepository.findByUsername(data.username()).isPresent()) {
            log.warn("User {} already exists", data.username());
            return ResponseEntity.badRequest().build();
        }

        var encryptedPassword = pwdEncoder.encode(data.password());

        User user = new User(
                data.username(), data.role().orElse(UserRole.USER), data.name(), data.lastName(), data.email(), encryptedPassword
        );

        userRepository.save(user);
        log.info("User {} registered", data.username());

        String token = tokenService.generateToken(user);

        ResponseCookie cookie = ResponseCookie.from("token", token).httpOnly(true).secure(false)
                .sameSite("Strict").path("/").maxAge(86400).build();

        return ResponseEntity.ok().header("Set-Cookie", cookie.toString()).body(new LoginResponseDTO(token, user));

    }
}
