package web.common;

import static org.slf4j.LoggerFactory.getLogger;

import domain.common.exception.AuthorizationException;
import domain.common.exception.EntityAlreadyExistsException;
import domain.common.exception.EntityNotExistsException;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
final class GlobalExceptionHandler {

    private static final Logger logger = getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(EntityNotExistsException.class)
    public void handleConflict(EntityNotExistsException e) {
        logger.error("Entity with name: {} and id: {} not exists", e.getEntityClass().getSimpleName(), e.getId());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(EntityAlreadyExistsException.class)
    public void handleConflict(EntityAlreadyExistsException e) {
        logger.error("Entity with name: {} and id: {} already exists", e.getEntityClass().getSimpleName(), e.getId());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthorizationException.class)
    public void handleConflict(AuthorizationException e) {
        logger.error("User with id: {} has no privilege to resource with id: {}", e.getUserId(), e.getResourceId());
    }
}
