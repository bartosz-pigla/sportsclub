package web.user;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import static query.model.user.repository.UserQueryExpressions.idMatches;
import static query.model.user.repository.UserQueryExpressions.usernameMatches;
import static web.common.RequestMappings.DIRECTOR_API_CUSTOMER;
import static web.common.RequestMappings.DIRECTOR_API_DIRECTOR;
import static web.common.RequestMappings.DIRECTOR_API_RECEPTIONIST;
import static web.common.RequestMappings.DIRECTOR_API_USER_ACTIVATE;
import static web.common.RequestMappings.DIRECTOR_API_USER_BY_ID;
import static web.user.dto.UserDtoFactory.create;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import api.user.command.ActivateUserCommand;
import api.user.command.DeactivateUserCommand;
import api.user.command.DeleteUserCommand;
import commons.ErrorCode;
import domain.user.activation.common.exception.AlreadyActivatedException;
import domain.user.activation.common.exception.AlreadyDeactivatedException;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import query.model.user.UserEntity;
import query.model.user.UserType;
import web.common.dto.FieldErrorDto;
import web.user.dto.CreateUserWebCommand;
import web.user.dto.UserActivationWebCommand;
import web.user.service.CreateUserService;
import web.user.service.CreateUserWebCommandValidator;

@RestController
@Setter(onMethod_ = { @Autowired })
final class UserDirectorController extends UserBaseController {

    private CreateUserService createUserService;
    private CreateUserWebCommandValidator validator;

    @InitBinder("createUserWebCommand")
    private void initValidation(WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @PostMapping(DIRECTOR_API_CUSTOMER)
    ResponseEntity<?> createUser(@RequestBody @Validated CreateUserWebCommand customer,
                                 BindingResult bindingResult) {
        return createUser(customer, UserType.CUSTOMER, bindingResult);
    }

    @PostMapping(DIRECTOR_API_DIRECTOR)
    ResponseEntity<?> createDirector(@RequestBody @Validated CreateUserWebCommand director,
                                     BindingResult bindingResult) {
        return createUser(director, UserType.DIRECTOR, bindingResult);
    }

    @PostMapping(DIRECTOR_API_RECEPTIONIST)
    ResponseEntity<?> createReceptionist(@RequestBody @Validated CreateUserWebCommand receptionist,
                                         BindingResult bindingResult) {
        return createUser(receptionist, UserType.RECEPTIONIST, bindingResult);
    }

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

    @DeleteMapping(DIRECTOR_API_USER_BY_ID)
    ResponseEntity<?> delete(@PathVariable UUID userId) {
        if (userRepository.exists(idMatches(userId))) {
            commandGateway.sendAndWait(new DeleteUserCommand(userId));
            return noContent().build();
        } else {
            return badRequest().build();
        }
    }

    private ResponseEntity<?> createUser(CreateUserWebCommand userWebCommand,
                                         UserType userType,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return errorResponseService.create(bindingResult);
        }

        createUserService.create(userWebCommand, userType);
        Optional<UserEntity> userOptional = userRepository.findOne(usernameMatches(userWebCommand.getUsername()));

        return userOptional.<ResponseEntity<?>> map(user -> ok(create(user)))
                .orElse(errorResponseService.create("username", ErrorCode.NOT_EXISTS, HttpStatus.CONFLICT));
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
