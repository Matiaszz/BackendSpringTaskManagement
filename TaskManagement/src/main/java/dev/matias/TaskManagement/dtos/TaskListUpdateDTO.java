package dev.matias.TaskManagement.dtos;

public record TaskListUpdateDTO(
                String title,
                String shortDescription,
                String longDescription,
                String color) {
}
