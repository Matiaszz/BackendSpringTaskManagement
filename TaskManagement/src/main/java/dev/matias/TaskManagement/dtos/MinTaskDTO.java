package dev.matias.TaskManagement.dtos;

import java.util.UUID;

public record MinTaskDTO(
        UUID id,
        String name,
        String shortDescription,
        Boolean isDone
){}
