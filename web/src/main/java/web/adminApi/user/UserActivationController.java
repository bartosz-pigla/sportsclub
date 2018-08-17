package web.adminApi.user;

import static org.springframework.http.ResponseEntity.ok;
import static query.model.user.repository.UserQueryExpressions.usernameMatches;
import static web.common.RequestMappings.ADMIN_API_USER_ACTIVATION;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import api.user.command.ActivateUserCommand;
import api.user.command.DeactivateUserCommand;
import com.google.common.collect.ImmutableList;
import commons.ErrorCode;
import domain.user.activation.common.exception.AlreadyActivatedException;
import domain.user.activation.common.exception.AlreadyDeactivatedException;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import query.model.user.UserEntity;
import query.model.user.dto.UserDto;
import web.adminApi.user.dto.UserActivationWebCommand;
import web.common.dto.FieldErrorDto;
import web.common.user.UserBaseController;

@RestController
@Setter(onMethod_ = { @Autowired })
final class UserActivationController extends UserBaseController {

    @PostMapping(ADMIN_API_USER_ACTIVATION)
    ResponseEntity<?> activateOrDeactivateUser(@RequestBody UserActivationWebCommand userActivationCommand) {
        Optional<UserEntity> userOptional = userRepository.findOne(
                usernameMatches(userActivationCommand.getUsername()));

        if (userOptional.isPresent()) {
            UserEntity userEntity = userOptional.get();
            sendActivationOrDeactivationCommand(userEntity.getId(), userActivationCommand.isActivated());

            return ok(UserDto.builder()
                    .username(userEntity.getUsername())
                    .userType(userEntity.getUserType().name())
                    .phoneNumber(userEntity.getPhoneNumber().getPhoneNumber())
                    .email(userEntity.getEmail().getEmail())
                    .deleted(userEntity.isDeleted())
                    .activated(userEntity.isActivated()).build());
        } else {
            return new ResponseEntity<>(
                    ImmutableList.of(new FieldErrorDto("username", ErrorCode.NOT_EXISTS)), HttpStatus.CONFLICT);
        }
    }

    private void sendActivationOrDeactivationCommand(UUID userId, boolean isActivated) {
        if (isActivated) {
            commandGateway.sendAndWait(ActivateUserCommand.builder()
                    .userId(userId).build());
        } else {
            commandGateway.sendAndWait(DeactivateUserCommand.builder()
                    .userId(userId).build());
        }
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyActivatedException.class)
    public List<FieldErrorDto> handleUserAlreadyActivatedConflict() {
        return ImmutableList.of(new FieldErrorDto("username", ErrorCode.ALREADY_ACTIVATED));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyDeactivatedException.class)
    public List<FieldErrorDto> handleUserAlreadyDeactivatedConflict() {
        return ImmutableList.of(new FieldErrorDto("username", ErrorCode.ALREADY_DEACTIVATED));
    }
}
