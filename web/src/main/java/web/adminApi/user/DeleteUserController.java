package web.adminApi.user;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.noContent;
import static query.model.user.repository.UserQueryExpressions.idMatches;
import static web.common.RequestMappings.DIRECTOR_API_CUSTOMER_BY_ID;
import static web.common.RequestMappings.DIRECTOR_API_DIRECTOR_BY_ID;
import static web.common.RequestMappings.DIRECTOR_API_RECEPTIONIST_BY_ID;

import java.util.UUID;

import api.user.command.DeleteUserCommand;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import web.common.user.UserBaseController;

@RestController
@Setter(onMethod_ = { @Autowired })
final class DeleteUserController extends UserBaseController {

    @DeleteMapping(DIRECTOR_API_CUSTOMER_BY_ID)
    ResponseEntity<?> deleteCustomer(@PathVariable UUID userId) {
        return deleteUser(userId);
    }

    @DeleteMapping(DIRECTOR_API_DIRECTOR_BY_ID)
    ResponseEntity<?> deleteDirector(@PathVariable UUID userId) {
        return deleteUser(userId);
    }

    @DeleteMapping(DIRECTOR_API_RECEPTIONIST_BY_ID)
    ResponseEntity<?> deleteReceptionist(@PathVariable UUID userId) {
        return deleteUser(userId);
    }

    private ResponseEntity<?> deleteUser(UUID userId) {
        boolean userExists = userRepository.exists(idMatches(userId));

        if (userExists) {
            commandGateway.sendAndWait(new DeleteUserCommand(userId));
            return noContent().build();
        } else {
            return badRequest().build();
        }
    }
}
