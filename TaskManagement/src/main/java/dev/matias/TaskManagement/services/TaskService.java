package dev.matias.TaskManagement.services;

import dev.matias.TaskManagement.domain.Task;
import dev.matias.TaskManagement.domain.TaskList;
import dev.matias.TaskManagement.dtos.MaxTaskDTO;
import dev.matias.TaskManagement.dtos.MinTaskDTO;
import dev.matias.TaskManagement.requests.TaskRequest;
import dev.matias.TaskManagement.repositories.TaskListRepository;
import dev.matias.TaskManagement.repositories.TaskRepository;
import dev.matias.TaskManagement.validations.PostTaskValidations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskListRepository taskListRepository;

    private PostTaskValidations postTaskValidations;

    public List<MinTaskDTO> getMinTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(task -> new MinTaskDTO(
                task.getId(), task.getName(), task.getShortDescription(), task.getIsDone())).toList();
    }

    public List<MaxTaskDTO> getMaxTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(task -> new MaxTaskDTO(
                task.getId(), task.getName(), task.getShortDescription(), task.getLongDescription(), task.getIsDone()))
                .toList();
    }

    public ResponseEntity<Task> postTask(TaskRequest taskRequest) {
        postTaskValidations.validatePostTask(taskRequest);

        TaskList taskList = taskListRepository.findById(taskRequest.taskListId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task list not found"));

        Task task = new Task(taskRequest.name(), taskRequest.shortDescription(), taskRequest.longDescription(), taskList);

        taskRepository.save(task);
        taskList.addTask(task);

        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }
}

