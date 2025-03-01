package dev.matias.TaskManagement.controllers;

import dev.matias.TaskManagement.domain.User;
import dev.matias.TaskManagement.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        String password = user.getPassword();
        if (password == null || password.isEmpty()) return ResponseEntity.badRequest().build();


        return ResponseEntity.ok(userRepository.save(user));
    }
}
