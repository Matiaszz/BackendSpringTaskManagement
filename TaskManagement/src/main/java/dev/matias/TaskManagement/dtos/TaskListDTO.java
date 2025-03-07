package dev.matias.TaskManagement.dtos;

import dev.matias.TaskManagement.domain.TaskList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record TaskListDTO(
        UUID id,
        String title,
        String shortDescription,
        String longDescription,
        String color,
        List<MaxTaskDTO> tasks
) {
    public TaskListDTO(TaskList taskList) {
        this(
                taskList.getId(),
                taskList.getTitle(),
                taskList.getShortDescription(),
                taskList.getLongDescription(),
                taskList.getColor(),
                taskList.getTasks().stream()
                        .map(MaxTaskDTO::new)
                        .collect(Collectors.toList())
        );
    }
}
