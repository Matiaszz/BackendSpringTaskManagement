package dev.matias.TaskManagement.dtos;

import dev.matias.TaskManagement.domain.UserRole;

import java.util.Optional;

public record RegisterDTO(
        String username,
        String name,
        String lastName,
        String email,
        String password,
        Optional<UserRole> role
) {
}
