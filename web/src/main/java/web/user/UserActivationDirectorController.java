package web.user;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.noContent;
import static query.model.user.repository.UserQueryExpressions.idMatches;
import static web.common.RequestMappings.DIRECTOR_API_USER_ACTIVATE;

import java.util.List;
import java.util.UUID;

import api.user.command.ActivateUserCommand;
import api.user.command.DeactivateUserCommand;
import commons.ErrorCode;
import domain.user.activation.common.exception.AlreadyActivatedException;
import domain.user.activation.common.exception.AlreadyDeactivatedException;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import web.user.dto.UserActivationWebCommand;
import web.common.dto.FieldErrorDto;

@RestController
@Setter(onMethod_ = { @Autowired })
final class UserActivationDirectorController extends UserBaseController {

    @PatchMapping(DIRECTOR_API_USER_ACTIVATE)
    ResponseEntity<?> activateOrDeactivateUser(@PathVariable UUID userId,
                                               @RequestBody UserActivationWebCommand userActivationCommand) {
        boolean userExists = userRepository.exists(idMatches(userId));

        if (userExists) {
            sendActivationOrDeactivationCommand(userId, userActivationCommand.isActivated());
            return noContent().build();
        } else {
            return badRequest().build();
        }
    }

    private void sendActivationOrDeactivationCommand(UUID userId, boolean isActivated) {
        if (isActivated) {
            commandGateway.sendAndWait(new ActivateUserCommand(userId));
        } else {
            commandGateway.sendAndWait(new DeactivateUserCommand(userId));
        }
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyActivatedException.class)
    public List<FieldErrorDto> handleUserAlreadyActivatedConflict() {
        return errorResponseService.createBody("userId", ErrorCode.ALREADY_ACTIVATED);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyDeactivatedException.class)
    public List<FieldErrorDto> handleUserAlreadyDeactivatedConflict() {
        return errorResponseService.createBody("userId", ErrorCode.ALREADY_DEACTIVATED);
    }
}
