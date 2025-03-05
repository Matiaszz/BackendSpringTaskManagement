package dev.matias.TaskManagement.dtos;

import java.util.List;

public record TaskListUpdateDTO(
        String title,
        String shortDescription,
        String longDescription,
        String color
) {
}
