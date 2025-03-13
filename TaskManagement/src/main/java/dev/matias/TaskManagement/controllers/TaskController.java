package dev.matias.TaskManagement.controllers;

import dev.matias.TaskManagement.domain.Task;
import dev.matias.TaskManagement.domain.TaskList;
import dev.matias.TaskManagement.dtos.MaxTaskDTO;
import dev.matias.TaskManagement.dtos.TaskUpdateDTO;
import dev.matias.TaskManagement.requests.TaskRequest;
import dev.matias.TaskManagement.repositories.TaskListRepository;
import dev.matias.TaskManagement.repositories.TaskRepository;
import dev.matias.TaskManagement.services.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskListRepository taskListRepository;

    @Autowired
    private TaskService taskService;

    @GetMapping("/allTasks")
    public ResponseEntity<List<MaxTaskDTO>> getTasks() {
        log.info("Getting All tasks (Admin)");
        return ResponseEntity.ok(taskService.getMaxTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaxTaskDTO> getTask(@PathVariable UUID id) {
        log.info("Getting the task with id: {}", id);
        return taskService.getTask(id);
    }

    @GetMapping("/taskList/{id}")
    public ResponseEntity<List<MaxTaskDTO>> getTasksByTaskListId(@PathVariable UUID id) {
        List<MaxTaskDTO> tasks = taskService.getListByTaskListId(id);
        log.info("Getting task by taskList id: {}", id);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<MaxTaskDTO> postTask(@RequestBody TaskRequest taskRequest) {
        log.info("Posting task...");
        TaskList taskList = taskListRepository.findById(taskRequest.taskListId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task list not found"));

        Task task = taskService.postTask(taskRequest, taskList).getBody();

        if (task == null) {
            log.error("Bad request: task is null.");
            return ResponseEntity.badRequest().build();
        }
        log.info("Task posted.");
        return ResponseEntity.ok(new MaxTaskDTO(task));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaxTaskDTO> updateTask(@RequestBody TaskUpdateDTO task, @PathVariable UUID id) {
        Task foundTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found."));

        log.info("Task with id {} updated.", id);
        return ResponseEntity.ok(taskService.updateTask(foundTask, task));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found "));

        taskService.deleteTask(task);
        log.info("Task deleted.");
        return ResponseEntity.noContent().build();

    }
}
