package dev.matias.TaskManagement.dtos;

import java.util.List;
import java.util.UUID;

public record TaskListDTO(
        UUID id,
        String title,
        String shortDescription,
        String longDescription,
        List<MaxTaskDTO> taskList
){}
