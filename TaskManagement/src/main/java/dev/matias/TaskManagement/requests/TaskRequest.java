package dev.matias.TaskManagement.requests;

import java.util.UUID;

public record TaskRequest(
        String name,
        String shortDescription,
        String longDescription,
        UUID taskListId
) {}
