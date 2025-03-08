package dev.matias.TaskManagement.controllers;

import dev.matias.TaskManagement.domain.Task;
import dev.matias.TaskManagement.domain.TaskList;
import dev.matias.TaskManagement.dtos.MaxTaskDTO;
import dev.matias.TaskManagement.requests.TaskRequest;
import dev.matias.TaskManagement.repositories.TaskListRepository;
import dev.matias.TaskManagement.repositories.TaskRepository;
import dev.matias.TaskManagement.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

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
    public ResponseEntity<List<MaxTaskDTO>> getTasks(){
        return ResponseEntity.ok(taskService.getMaxTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaxTaskDTO> getTask(@PathVariable UUID id){
        return taskService.getTask(id);
    }

    @GetMapping("/taskList/{id}")
    public ResponseEntity<List<MaxTaskDTO>> getTasksByTaskListId(@PathVariable UUID id){
        List<MaxTaskDTO> tasks = taskService.getListByTaskListId(id);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<MaxTaskDTO> postTask(@RequestBody TaskRequest taskRequest){

        TaskList taskList = taskListRepository.findById(taskRequest.taskListId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task list not found"));

        Task task = taskService.postTask(taskRequest, taskList).getBody();

        if (task == null) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(
                new MaxTaskDTO(task.getId(), task.getName(), task.getShortDescription(), task.getLongDescription(), task.getIsDone()));
    }
}
