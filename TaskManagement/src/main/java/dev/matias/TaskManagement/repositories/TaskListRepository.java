package dev.matias.TaskManagement.repositories;

import dev.matias.TaskManagement.domain.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskListRepository extends JpaRepository<TaskList, UUID> {

    @Query("SELECT tl FROM TaskList tl LEFT JOIN FETCH tl.tasks")
    List<TaskList> findAllWithTasks();

    List<TaskList> findByOwnerId(UUID id);
}
