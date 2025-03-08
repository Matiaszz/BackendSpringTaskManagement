package dev.matias.TaskManagement.dtos;

import java.util.UUID;

public record TaskUpdateDTO(
        String name,
        String shortDescription,
        String longDescription,
        Boolean isDone
) {
}
