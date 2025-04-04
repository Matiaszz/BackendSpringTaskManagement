package dev.matias.TaskManagement.services;

import dev.matias.TaskManagement.domain.Task;
import dev.matias.TaskManagement.domain.TaskList;
import dev.matias.TaskManagement.dtos.MaxTaskDTO;
import dev.matias.TaskManagement.dtos.TaskUpdateDTO;
import dev.matias.TaskManagement.repositories.TaskListRepository;
import dev.matias.TaskManagement.requests.TaskRequest;
import dev.matias.TaskManagement.repositories.TaskRepository;
import dev.matias.TaskManagement.validations.PostTaskValidations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private PostTaskValidations postTaskValidations;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private TaskListRepository taskListRepository;

    public List<MaxTaskDTO> getListByTaskListId(UUID taskListId) {
        UserDetails loggedUser = authorizationService.getLoggedUser();
        TaskList taskList = taskListRepository.findById(taskListId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task List not found."));

        if (!taskList.getOwner().getUsername().equalsIgnoreCase(loggedUser.getUsername())) {
            log.error("TaskList owner username is different of the following username: {}. (GET by TaskListId)", loggedUser.getUsername());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tasks not found.");
        }
        return taskList.getTasks().stream().map(MaxTaskDTO::new).toList();
    }

    public ResponseEntity<MaxTaskDTO> getTask(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
        UserDetails loggedUser = authorizationService.getLoggedUser();

        if (!task.getTaskList().getOwner().getUsername().equalsIgnoreCase(loggedUser.getUsername())) {
            log.error("TaskList owner username is different of the following username: {}. (GET by ID)", loggedUser.getUsername());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task Not found.");
        }
        log.info("Getting task: {}", task.getId());
        return ResponseEntity.ok(new MaxTaskDTO(task));
    }

    // TODO: REMOVE THIS
    public List<MaxTaskDTO> getMaxTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(MaxTaskDTO::new).toList();
    }

    public ResponseEntity<Task> postTask(TaskRequest taskRequest, TaskList taskList) {
        postTaskValidations.validatePostTask(taskRequest);
        System.out.println("\n");
        log.info("Validated...");

        Task task = new Task(taskRequest.name(), taskRequest.shortDescription(), taskRequest.longDescription(),
                taskList);

        taskRepository.save(task);
        taskList.addTask(task);
        log.info("Task created...");
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    public MaxTaskDTO updateTask(Task taskToUpdate, TaskUpdateDTO body) {
        UserDetails loggedUser = authorizationService.getLoggedUser();

        if (!taskToUpdate.getTaskList().getOwner().getUsername().equalsIgnoreCase(loggedUser.getUsername())) {
            log.error("TaskList owner username is different of the following username: {}. (PUT)", loggedUser.getUsername());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found.");
        }

        taskToUpdate.setName((body.name() != null) ? body.name() : taskToUpdate.getName());
        taskToUpdate.setLongDescription(
                (body.longDescription() != null) ? body.longDescription() : taskToUpdate.getLongDescription());
        taskToUpdate.setShortDescription(
                (body.shortDescription() != null) ? body.shortDescription() : taskToUpdate.getShortDescription());
        taskToUpdate.setIsDone((body.isDone() != null) ? body.isDone() : taskToUpdate.getIsDone());
        taskToUpdate.setUpdatedAt(LocalDateTime.now());

        taskRepository.save(taskToUpdate);
        log.info("Task updated.");
        return new MaxTaskDTO(taskToUpdate);
    }

    public void deleteTask(Task task) {
        UserDetails loggedUser = authorizationService.getLoggedUser();
        if (!task.getTaskList().getOwner().getUsername().equalsIgnoreCase(loggedUser.getUsername())) {
            log.error("TaskList owner username is different of the following username: {}. (DEL)", loggedUser.getUsername());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found.");
        }
        log.info("Task Deleted.");
        taskRepository.delete(task);
    }
}
