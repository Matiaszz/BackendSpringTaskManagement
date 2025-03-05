package dev.matias.TaskManagement.dtos;

import dev.matias.TaskManagement.domain.Task;

import java.util.UUID;

public record MaxTaskDTO(
        UUID id,
        String name,
        String shortDescription,
        String longDescription,
        Boolean isDone
) {
    public MaxTaskDTO(Task task) {
        this(
                task.getId(),
                task.getName(),
                task.getShortDescription(),
                task.getLongDescription(),
                task.getIsDone()
        );
    }
}
