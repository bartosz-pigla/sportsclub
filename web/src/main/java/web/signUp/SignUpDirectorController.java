package web.signUp;

import static web.common.RequestMappings.DIRECTOR;

import api.user.command.SendActivationLinkCommand;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import query.model.user.UserType;
import web.signUp.dto.CreateUserWebCommand;

@RestController
@Setter(onMethod_ = { @Autowired })
final class SignUpDirectorController extends AbstractSignUpController {

    @PostMapping(DIRECTOR)
    ResponseEntity<?> signUpDirector(@RequestBody @Validated CreateUserWebCommand director, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validationResponseService.getResponse(bindingResult);
        }

        signUpUser(director, UserType.DIRECTOR);
        userRepository.findByUsername(director.getUsername()).ifPresent(c ->
                commandGateway.sendAndWait(SendActivationLinkCommand.builder().customerId(c.getId()).build()));

        return ResponseEntity.ok(director);
    }
}
