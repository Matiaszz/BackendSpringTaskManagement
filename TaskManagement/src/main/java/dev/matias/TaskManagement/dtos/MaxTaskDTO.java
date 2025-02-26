package dev.matias.TaskManagement.dtos;

import java.util.UUID;

public record MaxTaskDTO (
    UUID id,
    String name,
    String shortDescription,
    String longDescription,
    Boolean isDone
){}
