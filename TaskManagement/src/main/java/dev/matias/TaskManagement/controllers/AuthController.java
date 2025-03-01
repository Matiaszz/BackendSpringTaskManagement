package dev.matias.TaskManagement.controllers;

import dev.matias.TaskManagement.domain.User;
import dev.matias.TaskManagement.dtos.AuthenticationDTO;
import dev.matias.TaskManagement.dtos.RegisterDTO;
import dev.matias.TaskManagement.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationDTO> login(@RequestBody @Valid AuthenticationDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = authenticationManager.authenticate(usernamePassword);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterDTO> register(@RequestBody @Valid RegisterDTO data){
        if (this.userRepository.findByUsername(data.username()) != null) return ResponseEntity.badRequest().build();
        var encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

        User user = new User(
                data.username(), data.role(), data.name(), data.lastName(), data.email(), encryptedPassword
        );

        userRepository.save(user);

        return ResponseEntity.ok().build();
    }
}
