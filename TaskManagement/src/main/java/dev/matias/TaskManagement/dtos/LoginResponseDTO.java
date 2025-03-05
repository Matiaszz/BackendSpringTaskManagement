package dev.matias.TaskManagement.dtos;

import java.util.UUID;

public record LoginResponseDTO(String token, UUID id) {
}
