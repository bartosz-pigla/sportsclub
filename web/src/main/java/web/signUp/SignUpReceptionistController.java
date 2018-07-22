package web.signUp;

import static web.common.RequestMappings.RECEPTIONIST;

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
final class SignUpReceptionistController extends AbstractSignUpController {

    @PostMapping(RECEPTIONIST)
    ResponseEntity<?> signUpReceptionist(@RequestBody @Validated CreateUserWebCommand receptionist, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validationResponseService.getResponse(bindingResult);
        }

        signUpUser(receptionist, UserType.RECEPTIONIST);
        userRepository.findByUsername(receptionist.getUsername()).ifPresent(c ->
                commandGateway.sendAndWait(SendActivationLinkCommand.builder().customerId(c.getId()).build()));

        return ResponseEntity.ok(receptionist);
    }
}
