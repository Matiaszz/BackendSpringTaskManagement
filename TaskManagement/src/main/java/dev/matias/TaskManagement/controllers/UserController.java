package dev.matias.TaskManagement.controllers;

import dev.matias.TaskManagement.domain.User;
import dev.matias.TaskManagement.dtos.UserUpdateDTO;
import dev.matias.TaskManagement.repositories.UserRepository;
import dev.matias.TaskManagement.services.AuthorizationService;
import dev.matias.TaskManagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private UserService userService;


    @GetMapping
    public String getUsers(){
        return userRepository.findAll().toString();
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody UserUpdateDTO updateDTO){
        UserDetails user = authorizationService.getLoggedUser();
        User newUser = userService.updateUser((User) user, updateDTO);

        return ResponseEntity.ok(newUser);

    }
}
