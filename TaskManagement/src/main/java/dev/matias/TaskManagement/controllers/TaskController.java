package dev.matias.TaskManagement.controllers;

import dev.matias.TaskManagement.domain.Task;
import dev.matias.TaskManagement.dtos.MaxTaskDTO;
import dev.matias.TaskManagement.dtos.MinTaskDTO;
import dev.matias.TaskManagement.requests.TaskRequest;
import dev.matias.TaskManagement.repositories.TaskListRepository;
import dev.matias.TaskManagement.repositories.TaskRepository;
import dev.matias.TaskManagement.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskListRepository taskListRepository;

    @Autowired
    private TaskService taskService;

    private MinTaskDTO minTaskDTO;


    @GetMapping("/short")
    public ResponseEntity<List<MinTaskDTO>> getTasks(){
        return ResponseEntity.ok(taskService.getMinTasks());
    }
    @GetMapping("/long")
    public ResponseEntity<List<MaxTaskDTO>> getLongTasks(){
        return ResponseEntity.ok(taskService.getMaxTasks());
    }
    @PostMapping
    public ResponseEntity<MinTaskDTO> postTask(@RequestBody TaskRequest taskRequest){
        Task task = taskService.postTask(taskRequest).getBody();

        if (task == null) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(
                new MinTaskDTO(task.getId(), task.getName(), task.getShortDescription(), task.getIsDone()));
    }
}
