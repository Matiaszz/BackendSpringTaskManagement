package dev.matias.TaskManagement.controllers;

import dev.matias.TaskManagement.domain.TaskList;
import dev.matias.TaskManagement.dtos.TaskListDTO;
import dev.matias.TaskManagement.repositories.TaskListRepository;
import dev.matias.TaskManagement.services.TaskListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/taskList")
public class TaskListController {

    @Autowired
    private TaskListRepository taskListRepository;

    @Autowired
    private TaskListService taskListService;



    @GetMapping
    public ResponseEntity <List<TaskListDTO>> getTaskList(){
        return ResponseEntity.ok(taskListService.getTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskListDTO> getTaskListById(@PathVariable UUID id){
        TaskList taskList = taskListRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Task list not found"));

        return ResponseEntity.ok(taskListService.getTaskList(taskList.getId()));
    }

    @PostMapping
    public ResponseEntity<TaskList> createTaskList(@RequestBody TaskList taskList){
        taskListRepository.save(taskList);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
