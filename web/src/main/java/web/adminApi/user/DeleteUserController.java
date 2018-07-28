package web.adminApi.user;

import static web.common.RequestMappings.ADMIN_CONSOLE_CUSTOMER_BY_USERNAME;
import static web.common.RequestMappings.ADMIN_CONSOLE_DIRECTOR_BY_USERNAME;
import static web.common.RequestMappings.ADMIN_CONSOLE_RECEPTIONIST_BY_USERNAME;

import java.util.Optional;

import api.user.command.DeleteUserCommand;
import commons.ErrorCode;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import query.model.user.UserEntity;
import web.common.UserBaseController;
import web.common.dto.DeleteUserWebCommand;

@RestController
@Setter(onMethod_ = { @Autowired })
final class DeleteUserController extends UserBaseController {

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
            return validationResponseService.getOneFieldErrorResponse("username", ErrorCode.NOT_EXISTS);
        }
    }
}
