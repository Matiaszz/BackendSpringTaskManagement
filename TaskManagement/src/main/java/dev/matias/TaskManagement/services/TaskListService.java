package dev.matias.TaskManagement.services;

import dev.matias.TaskManagement.domain.TaskList;
import dev.matias.TaskManagement.dtos.MaxTaskDTO;
import dev.matias.TaskManagement.dtos.TaskListDTO;
import dev.matias.TaskManagement.repositories.TaskListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TaskListService {

    @Autowired
    private TaskListRepository taskListRepository;

    private MaxTaskDTO maxTaskDTO;

    public List<TaskListDTO> getTasks() {
        List<TaskList> taskLists = taskListRepository.findAll();

        return taskLists.stream().map(taskList -> new TaskListDTO(
                taskList.getId(),
                taskList.getTitle(),
                taskList.getShortDescription(),
                taskList.getLongDescription(),
                taskList.getTasks().stream().map(
                        task -> new MaxTaskDTO(
                        task.getId(),
                        task.getName(),
                        task.getShortDescription(),
                        task.getLongDescription(),
                        task.getIsDone()
                )).collect(Collectors.toList())
        )
        ).toList();
    }

    public TaskListDTO getTaskList(UUID id){
        TaskList taskList = taskListRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task list not found"));

        return new TaskListDTO(taskList.getId(), taskList.getTitle(), taskList.getShortDescription(),
                taskList.getLongDescription(), taskList.getTasks().stream().map(task -> new MaxTaskDTO(

                task.getId(),
                task.getName(),
                task.getShortDescription(),
                task.getLongDescription(),
                task.getIsDone()
        )).collect(Collectors.toList()));
    }
}
