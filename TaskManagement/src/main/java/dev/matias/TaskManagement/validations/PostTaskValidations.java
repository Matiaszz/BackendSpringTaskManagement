package dev.matias.TaskManagement.validations;

import dev.matias.TaskManagement.requests.TaskRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

public class PostTaskValidations {
    public ResponseEntity<Object> validatePostTask(TaskRequest taskRequest) {
        if (taskRequest == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Body can't be null");

        // Nullable validators
        if (taskRequest.taskListId() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'taskListId' field can't be null");
        if (taskRequest.name() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'name' field can't be null");
        if (taskRequest.shortDescription() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'shortDescription' field can't be null");
        if (taskRequest.longDescription() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'longDescription' field can't be null");

        if (taskRequest.name().length() > 40) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'name' field character limit is 40");
        if (taskRequest.shortDescription().length() > 80) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'shortDescription' field character limit is 80");
        return null;
    }
}
