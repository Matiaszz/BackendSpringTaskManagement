package dev.matias.TaskManagement.controllers;

import dev.matias.TaskManagement.domain.Task;
import dev.matias.TaskManagement.domain.TaskList;
import dev.matias.TaskManagement.domain.User;
import dev.matias.TaskManagement.dtos.TaskListDTO;
import dev.matias.TaskManagement.dtos.TaskListUpdateDTO;
import dev.matias.TaskManagement.repositories.TaskListRepository;
import dev.matias.TaskManagement.repositories.UserRepository;
import dev.matias.TaskManagement.requests.TaskListRequest;
import dev.matias.TaskManagement.services.AuthorizationService;
import dev.matias.TaskManagement.services.TaskListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/taskList")
public class TaskListController {

    @Autowired
    private TaskListRepository taskListRepository;

    @Autowired
    private TaskListService taskListService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorizationService authorizationService;

    @GetMapping
    public ResponseEntity <List<TaskListDTO>> getTaskList(){
        return ResponseEntity.ok(taskListService.getTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskListDTO> getTaskListById(@PathVariable UUID id){
        TaskList taskList = taskListRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task list not found"));

        UserDetails loggedUser = authorizationService.getLoggedUser();
        if (!loggedUser.getUsername().equalsIgnoreCase(taskList.getOwner().getUsername())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task List not found (wrong user)");
        }
        return ResponseEntity.ok(new TaskListDTO(taskList));
    }

    @PostMapping
    public ResponseEntity<TaskList> createTaskList(@RequestBody TaskListRequest request){
        TaskList taskList = new TaskList(authorizationService, request.title(), request.shortDescription(), request.longDescription());
        taskListRepository.save(taskList);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskListDTO> updateTaskList(@PathVariable UUID id, @RequestBody TaskListUpdateDTO taskListUpdateDTO){
        TaskList taskList = taskListRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "TaskList not found for update."));

        TaskListDTO dto = taskListService.updateTaskList(taskList, taskListUpdateDTO);
        return ResponseEntity.ok(dto);
    }
}
