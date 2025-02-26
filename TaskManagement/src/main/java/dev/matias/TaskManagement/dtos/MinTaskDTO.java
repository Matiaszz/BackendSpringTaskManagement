package dev.matias.TaskManagement.dtos;

import dev.matias.TaskManagement.domain.Task;

import java.time.LocalDateTime;
import java.util.UUID;

public record MinTaskDTO(
        UUID id,
        String name,
        String shortDescription,
        Boolean isDone
){}
