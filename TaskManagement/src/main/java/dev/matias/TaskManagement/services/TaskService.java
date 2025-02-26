package dev.matias.TaskManagement.services;

import dev.matias.TaskManagement.domain.Task;
import dev.matias.TaskManagement.domain.TaskList;
import dev.matias.TaskManagement.dtos.MinTaskDTO;
import dev.matias.TaskManagement.requests.TaskRequest;
import dev.matias.TaskManagement.repositories.TaskListRepository;
import dev.matias.TaskManagement.repositories.TaskRepository;
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

    public List<MinTaskDTO> getTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(task -> new MinTaskDTO(task.getId(), task.getName(), task.getShortDescription(), task.getIsDone())).toList();
    }

    public ResponseEntity<Task> postTask(TaskRequest taskRequest) {
        TaskList taskList = taskListRepository.findById(taskRequest.taskListId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task list not found"));

        Task task = new Task();
        task.setName(taskRequest.name());
        task.setShortDescription(taskRequest.shortDescription());
        task.setLongDescription(taskRequest.longDescription());
        task.setTaskList(taskList);

        taskRepository.save(task);
        taskList.addTask(task);

        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }
}

