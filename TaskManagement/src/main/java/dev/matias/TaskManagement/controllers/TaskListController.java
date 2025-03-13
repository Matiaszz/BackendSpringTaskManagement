package dev.matias.TaskManagement.controllers;

import dev.matias.TaskManagement.domain.TaskList;
import dev.matias.TaskManagement.domain.User;
import dev.matias.TaskManagement.dtos.TaskListDTO;
import dev.matias.TaskManagement.dtos.TaskListUpdateDTO;
import dev.matias.TaskManagement.repositories.TaskListRepository;
import dev.matias.TaskManagement.requests.TaskListRequest;
import dev.matias.TaskManagement.services.AuthorizationService;
import dev.matias.TaskManagement.services.TaskListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/taskList")
public class TaskListController {

    @Autowired
    private TaskListRepository taskListRepository;

    @Autowired
    private TaskListService taskListService;

    @Autowired
    private AuthorizationService authorizationService;

    @GetMapping
    public ResponseEntity<List<TaskListDTO>> getTaskListByUser() {
        UserDetails loggedUser = authorizationService.getLoggedUser();
        log.info("Getting task by user: {}", loggedUser);
        return taskListService.getTaskListByUser((User) loggedUser);
    }

    // TODO: Remove this
    @GetMapping("/taskLists")
    public ResponseEntity<List<TaskListDTO>> getTaskList() {
        return ResponseEntity.ok(taskListService.getAllTaskLists());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskListDTO> getTaskListById(@PathVariable UUID id) {
        log.info("Getting taskList by id... {}", id);
        TaskList taskList = taskListRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task list not found"));

        UserDetails loggedUser = authorizationService.getLoggedUser();
        if (!loggedUser.getUsername().equalsIgnoreCase(taskList.getOwner().getUsername())) {
            log.error("TaskList is not from the logged user: {}", loggedUser.getUsername());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task List not found");
        }
        log.info("TaskList found.");
        return ResponseEntity.ok(new TaskListDTO(taskList));
    }

    @PostMapping
    public ResponseEntity<TaskList> createTaskList(@RequestBody TaskListRequest request) {
        log.info("Creating taskList...");

        TaskList taskList = new TaskList(authorizationService, request.title(), request.shortDescription(),
                request.longDescription());
        taskListRepository.save(taskList);
        log.info("TaskList created.");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskListDTO> updateTaskList(@PathVariable UUID id,
            @RequestBody TaskListUpdateDTO taskListUpdateDTO) {

        log.info("Updating taskList...");
        TaskList taskList = taskListRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "TaskList not found for update."));

        TaskListDTO dto = taskListService.updateTaskList(taskList, taskListUpdateDTO);
        log.info("TaskList Updated");
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaskList(@PathVariable UUID id) {
        log.info("Deleting taskList...");

        TaskList taskList = taskListRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task list not found"));

        taskListService.deleteTaskList(taskList);
        log.info("Task with id: {} Deleted.", taskList.getId());
        return ResponseEntity.ok().build();
    }
}
