package web.common.user;

import java.util.List;

import commons.ErrorCode;
import domain.common.exception.AlreadyCreatedException;
import domain.common.exception.AlreadyDeletedException;
import lombok.Setter;
import org.axonframework.commandhandling.model.AggregateNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import query.model.user.repository.UserEntityRepository;
import web.common.BaseController;
import web.common.dto.FieldErrorDto;

@Setter(onMethod_ = { @Autowired })
public abstract class UserBaseController extends BaseController {

    protected UserEntityRepository userRepository;

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyCreatedException.class)
    public List<FieldErrorDto> handleUserAlreadyCreatedConflict() {
        return errorResponseService.createBody("username", ErrorCode.ALREADY_EXISTS);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyDeletedException.class)
    public List<FieldErrorDto> handleUserAlreadyDeletedConflict() {
        return errorResponseService.createBody("userId", ErrorCode.ALREADY_DELETED);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AggregateNotFoundException.class)
    public List<FieldErrorDto> handleUserNotExists() {
        return errorResponseService.createBody("userId", ErrorCode.NOT_EXISTS);
    }
}
