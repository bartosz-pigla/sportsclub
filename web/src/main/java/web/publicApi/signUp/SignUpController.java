package web.publicApi.signUp;

import static query.model.user.repository.UserQueryExpressions.usernameMatches;
import static web.common.RequestMappings.SIGN_UP;

import api.user.command.SendActivationLinkCommand;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import query.model.user.UserType;
import web.adminApi.user.service.CreateUserService;
import web.common.user.UserBaseController;
import web.common.user.dto.CreateUserWebCommand;
import web.common.user.service.CreateUserWebCommandValidator;

@RestController
@Setter(onMethod_ = { @Autowired })
final class SignUpController extends UserBaseController {

    private CreateUserWebCommandValidator validator;
    private CreateUserService createUserService;

    @InitBinder
    private void initValidation(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @PostMapping(SIGN_UP)
    ResponseEntity<?> signUpCustomer(@RequestBody @Validated CreateUserWebCommand customer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validationResponseService.getResponse(bindingResult);
        }

        createUserService.create(customer, UserType.CUSTOMER);
        userRepository.findOne(usernameMatches(customer.getUsername())).ifPresent(c ->
                commandGateway.sendAndWait(SendActivationLinkCommand.builder().customerId(c.getId()).build()));

        return ResponseEntity.ok(customer);
    }
}
