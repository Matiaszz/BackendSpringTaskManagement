package dev.matias.TaskManagement.dtos;

import dev.matias.TaskManagement.domain.Task;
import dev.matias.TaskManagement.domain.TaskList;
import dev.matias.TaskManagement.services.TaskListService;

import java.util.List;
import java.util.UUID;

public record TaskListDTO(
        UUID id,
        String title,
        String shortDescription,
        String longDescription,
        List<MaxTaskDTO> taskList
){}
