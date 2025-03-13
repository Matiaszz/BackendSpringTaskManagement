package dev.matias.TaskManagement.validations;

import dev.matias.TaskManagement.domain.TaskList;
import dev.matias.TaskManagement.repositories.TaskListRepository;
import dev.matias.TaskManagement.requests.TaskRequest;
import dev.matias.TaskManagement.services.AuthorizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Component
public class PostTaskValidations {
    @Autowired
    private TaskListRepository taskListRepository;

    @Autowired
    private AuthorizationService authorizationService;

    public void validatePostTask(TaskRequest taskRequest) {
        if (taskRequest == null) {
            log.error("Validation failed: TaskRequest body is null.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Body can't be null");
        }

        TaskList taskList = taskListRepository.findById(taskRequest.taskListId())
                .orElseThrow(() -> {
                    log.error("TaskList with id {} not found.", taskRequest.taskListId());
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "TaskList not found");
                });

        UserDetails user = authorizationService.getLoggedUser();

        if (!taskList.getOwner().getUsername().equals(user.getUsername())) {
            log.error("User {} is not the owner of TaskList with id {}.", user.getUsername(), taskList.getId());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the owner of this TaskList");
        }

        if (taskRequest.name() == null) {
            log.error("Validation failed: 'name' field is null.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'name' field can't be null");
        }

        if (taskRequest.shortDescription() == null) {
            log.error("Validation failed: 'shortDescription' field is null.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'shortDescription' field can't be null");
        }

        if (taskRequest.longDescription() == null) {
            log.error("Validation failed: 'longDescription' field is null.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'longDescription' field can't be null");
        }

        if (taskRequest.name().length() > 40) {
            log.error("Validation failed: 'name' field exceeds 40 characters. Current length: {}", taskRequest.name().length());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'name' field character limit is 40");
        }

        if (taskRequest.shortDescription().length() > 80) {
            log.error("Validation failed: 'shortDescription' field exceeds 80 characters. Current length: {}", taskRequest.shortDescription().length());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'shortDescription' field character limit is 80");
        }

        log.info("TaskRequest validated successfully for TaskList with id {}.", taskList.getId());
    }
}
