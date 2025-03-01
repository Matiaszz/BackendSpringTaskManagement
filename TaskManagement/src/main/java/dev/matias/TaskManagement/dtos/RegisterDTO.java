package dev.matias.TaskManagement.dtos;

import dev.matias.TaskManagement.domain.UserRole;

public record RegisterDTO(String username, UserRole role, String name, String lastName, String email, String password) {
}
