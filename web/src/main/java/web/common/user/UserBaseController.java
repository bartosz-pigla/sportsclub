package web.common.user;

import commons.ErrorCode;
import domain.common.exception.AlreadyCreatedException;
import domain.common.exception.AlreadyDeletedException;
import lombok.Setter;
import org.axonframework.commandhandling.model.AggregateNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import query.model.user.repository.UserEntityRepository;
import web.common.BaseController;

@Setter(onMethod_ = { @Autowired })
public abstract class UserBaseController extends BaseController {

    protected UserEntityRepository userRepository;

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyCreatedException.class)
    public ResponseEntity<?> handleUserAlreadyCreatedConflict() {
        return validationResponseService.getOneFieldErrorResponse("username", ErrorCode.ALREADY_EXISTS);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyDeletedException.class)
    public ResponseEntity<?> handleUserAlreadyDeletedConflict() {
        return validationResponseService.getOneFieldErrorResponse("username", ErrorCode.ALREADY_DELETED);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AggregateNotFoundException.class)
    public ResponseEntity<?> handleUserNotExists() {
        return validationResponseService.getOneFieldErrorResponse("username", ErrorCode.NOT_EXISTS);
    }
}
