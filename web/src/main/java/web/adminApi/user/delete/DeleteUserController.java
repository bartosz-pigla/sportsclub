package web.adminApi.user.delete;

import static web.common.RequestMappings.ADMIN_CONSOLE_CUSTOMER_BY_USERNAME;
import static web.common.RequestMappings.ADMIN_CONSOLE_DIRECTOR_BY_USERNAME;
import static web.common.RequestMappings.ADMIN_CONSOLE_RECEPTIONIST_BY_USERNAME;

import java.util.Optional;

import api.user.command.DeleteUserCommand;
import commons.ErrorCode;
import domain.user.deleteUser.exception.UserAlreadyDeletedException;
import lombok.Setter;
import org.axonframework.commandhandling.model.AggregateNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import query.model.user.UserEntity;
import query.repository.UserEntityRepository;
import web.common.BaseController;
import web.common.dto.DeleteUserWebCommand;
import web.common.dto.FieldErrorDto;

@RestController
@Setter(onMethod_ = { @Autowired })
final class DeleteUserController extends BaseController {

    private UserEntityRepository userRepository;

    @DeleteMapping(ADMIN_CONSOLE_CUSTOMER_BY_USERNAME)
    ResponseEntity<?> deleteCustomer(@PathVariable String username) {
        return deleteUser(username);
    }

    @DeleteMapping(ADMIN_CONSOLE_DIRECTOR_BY_USERNAME)
    ResponseEntity<?> deleteDirector(@PathVariable String username) {
        return deleteUser(username);
    }

    @DeleteMapping(ADMIN_CONSOLE_RECEPTIONIST_BY_USERNAME)
    ResponseEntity<?> deleteReceptionist(@PathVariable String username) {
        return deleteUser(username);
    }

    private ResponseEntity<?> deleteUser(String username) {
        Optional<UserEntity> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            commandGateway.sendAndWait(DeleteUserCommand.builder()
                    .userId(user.getId()).build());

            return ResponseEntity.ok(DeleteUserWebCommand.builder()
                    .username(user.getUsername())
                    .email(user.getEmail().getEmail())
                    .phoneNumber(user.getPhoneNumber().getPhoneNumber()).build());
        } else {
            return validationResponseService.getResponse(
                    HttpStatus.CONFLICT,
                    new FieldErrorDto("username", ErrorCode.NOT_EXISTS.getCode()));
        }
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserAlreadyDeletedException.class)
    public ResponseEntity<?> handleUserAlreadyDeletedConflict() {
        return validationResponseService.getOneFieldErrorResponse("username", ErrorCode.ALREADY_DELETED);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AggregateNotFoundException.class)
    public ResponseEntity<?> handleUserNotExists() {
        return validationResponseService.getOneFieldErrorResponse("username", ErrorCode.NOT_EXISTS);
    }
}
