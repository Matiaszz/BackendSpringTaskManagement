package dev.matias.TaskManagement.repositories;

import dev.matias.TaskManagement.domain.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskListRepository extends JpaRepository<TaskList, UUID> {
}
