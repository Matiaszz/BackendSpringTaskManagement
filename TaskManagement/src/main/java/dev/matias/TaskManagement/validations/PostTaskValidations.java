package dev.matias.TaskManagement.validations;

import dev.matias.TaskManagement.domain.TaskList;
import dev.matias.TaskManagement.repositories.TaskListRepository;
import dev.matias.TaskManagement.requests.TaskRequest;
import dev.matias.TaskManagement.services.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class PostTaskValidations {
    @Autowired
    private TaskListRepository taskListRepository;

    @Autowired
    private AuthorizationService authorizationService;

    public void validatePostTask(TaskRequest taskRequest) {
        if (taskRequest == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Body can't be null");

        TaskList taskList = taskListRepository.findById(taskRequest.taskListId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "TaskList not found"));

        UserDetails user = authorizationService.getLoggedUser();

        if (!taskList.getOwner().getUsername().equals(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the owner of this TaskList");
        }

        if (taskRequest.name() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'name' field can't be null");
        if (taskRequest.shortDescription() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'shortDescription' field can't be null");
        if (taskRequest.longDescription() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'longDescription' field can't be null");

        if (taskRequest.name().length() > 40)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'name' field character limit is 40");
        if (taskRequest.shortDescription().length() > 80)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'shortDescription' field character limit is 80");
    }
}
