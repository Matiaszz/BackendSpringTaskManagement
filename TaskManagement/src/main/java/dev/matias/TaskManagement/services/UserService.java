package dev.matias.TaskManagement.services;

import dev.matias.TaskManagement.domain.User;
import dev.matias.TaskManagement.dtos.UserUpdateDTO;
import dev.matias.TaskManagement.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User updateUser(User authenticatedUser, UserUpdateDTO updateDTO) {
        boolean changed = false;

        if (updateDTO.name() != null && !updateDTO.name().trim().isEmpty()){
            if (!authenticatedUser.getName().trim().equals(updateDTO.name().trim())){
                authenticatedUser.setName(updateDTO.name().trim());
                changed = true;
            }
        }

        if (updateDTO.lastName() != null && !updateDTO.lastName().trim().isEmpty()) {
            if (!authenticatedUser.getLastName().trim().equals(updateDTO.lastName().trim())) {
                authenticatedUser.setLastName(updateDTO.lastName().trim());
                changed = true;
            }
        }

        if (updateDTO.email() != null && !updateDTO.email().trim().isEmpty()) {
            if (!authenticatedUser.getEmail().trim().equals(updateDTO.email().trim())) {
                // TODO: Make a Mail service in future
                authenticatedUser.setEmail(updateDTO.email().trim());
                changed = true;
            }
        }

        if (updateDTO.description() != null && !updateDTO.description().trim().isEmpty()) {
            if (!authenticatedUser.getDescription().trim().equals(updateDTO.description().trim())) {
                authenticatedUser.setDescription(updateDTO.description().trim());
                changed = true;
            }
        }

        if (updateDTO.profileImageURL() != null && !updateDTO.profileImageURL().trim().equals(authenticatedUser.getProfileImageURL().trim())) {
            authenticatedUser.setProfileImageURL(updateDTO.profileImageURL().trim());
            changed = true;
        }

        // TODO: Make a "forgot password" function

        if (changed) {
            return userRepository.save(authenticatedUser);
        }

        return authenticatedUser;
    }
}
