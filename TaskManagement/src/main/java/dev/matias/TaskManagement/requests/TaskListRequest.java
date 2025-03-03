package dev.matias.TaskManagement.requests;

import java.util.UUID;

public record TaskListRequest(
        String title,
        String shortDescription,
        String longDescription,
        UUID ownerId
) {}
