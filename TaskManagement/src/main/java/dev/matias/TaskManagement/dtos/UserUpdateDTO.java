package dev.matias.TaskManagement.dtos;

public record UserUpdateDTO(
        String name,
        String lastName,
        String email,
        String password,
        String description,
        String profileImageURL
) {

}
