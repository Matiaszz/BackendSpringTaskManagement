package dev.matias.TaskManagement.services;

import dev.matias.TaskManagement.domain.TaskList;
import dev.matias.TaskManagement.domain.User;
import dev.matias.TaskManagement.dtos.MaxTaskDTO;
import dev.matias.TaskManagement.dtos.TaskListDTO;
import dev.matias.TaskManagement.dtos.TaskListUpdateDTO;
import dev.matias.TaskManagement.repositories.TaskListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskListService {

    @Autowired
    private TaskListRepository taskListRepository;

    private MaxTaskDTO maxTaskDTO;

    @Autowired
    private AuthorizationService authorizationService;

    public List<TaskListDTO> getAllTaskLists() {
        List<TaskList> taskLists = taskListRepository.findAllWithTasks();

        return taskLists.stream().map(TaskListDTO::new).toList();
    }

    public ResponseEntity<List<TaskListDTO>> getTaskListByUser(User user){
        List<TaskList> taskLists = taskListRepository.findByOwnerId(user.getId());
        if (taskLists.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        List<TaskListDTO> dtoList = taskLists.stream().map(TaskListDTO::new).toList();
        return ResponseEntity.ok(dtoList);
    }

    public TaskListDTO updateTaskList(TaskList taskList, TaskListUpdateDTO taskListUpdateDTO){
        UserDetails loggedUser = authorizationService.getLoggedUser();

        if (!taskList.getOwner().getUsername().equalsIgnoreCase(loggedUser.getUsername())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "TaskList not found.");
        }

        taskList.setTitle((taskListUpdateDTO.title() != null) ? taskListUpdateDTO.title() : taskList.getTitle());

        taskList.setShortDescription((
                taskListUpdateDTO.shortDescription() != null) ? taskListUpdateDTO.shortDescription() : taskList.getShortDescription());

        taskList.setLongDescription((
                taskListUpdateDTO.longDescription() != null) ? taskListUpdateDTO.longDescription() : taskList.getLongDescription());

        taskList.setColor((taskListUpdateDTO.color() != null) ? taskListUpdateDTO.color() : taskList.getColor());
        return new TaskListDTO(taskList);
    }

    public void deleteTaskList(TaskList taskList){
        UserDetails loggedUser = authorizationService.getLoggedUser();

        if (!loggedUser.getUsername().equalsIgnoreCase(taskList.getOwner().getUsername())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task list not found.");
        }

        taskListRepository.deleteById(taskList.getId());
    }
}
