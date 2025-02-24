package dev.matias.TaskManagement.repositories;

import dev.matias.TaskManagement.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
}
