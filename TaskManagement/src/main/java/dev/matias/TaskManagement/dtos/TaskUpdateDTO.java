package dev.matias.TaskManagement.dtos;

public record TaskUpdateDTO(
                String name,
                String shortDescription,
                String longDescription,
                Boolean isDone) {
}
