package web.adminApi.user;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;
import static query.model.user.repository.UserQueryExpressions.usernameMatches;
import static web.common.RequestMappings.ADMIN_API_CUSTOMER_BY_USERNAME;
import static web.common.RequestMappings.ADMIN_API_DIRECTOR_BY_USERNAME;
import static web.common.RequestMappings.ADMIN_API_RECEPTIONIST_BY_USERNAME;

import java.util.Optional;

import api.user.command.DeleteUserCommand;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import query.model.user.UserEntity;
import query.model.user.dto.UserDto;
import web.common.user.UserBaseController;

@RestController
@Setter(onMethod_ = { @Autowired })
final class DeleteUserController extends UserBaseController {

    @DeleteMapping(ADMIN_API_CUSTOMER_BY_USERNAME)
    ResponseEntity<?> deleteCustomer(@PathVariable String username) {
        return deleteUser(username);
    }

    @DeleteMapping(ADMIN_API_DIRECTOR_BY_USERNAME)
    ResponseEntity<?> deleteDirector(@PathVariable String username) {
        return deleteUser(username);
    }

    @DeleteMapping(ADMIN_API_RECEPTIONIST_BY_USERNAME)
    ResponseEntity<?> deleteReceptionist(@PathVariable String username) {
        return deleteUser(username);
    }

    private ResponseEntity<?> deleteUser(String username) {
        Optional<UserEntity> userOptional = userRepository.findOne(
                usernameMatches(username));

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            commandGateway.sendAndWait(DeleteUserCommand.builder()
                    .userId(user.getId()).build());

            return ok(UserDto.builder()
                    .username(user.getUsername())
                    .email(user.getEmail().getEmail())
                    .phoneNumber(user.getPhoneNumber().getPhoneNumber()).build());
        } else {
            return badRequest().build();
        }
    }
}
