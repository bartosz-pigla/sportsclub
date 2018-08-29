package web.signUp;

import static org.springframework.http.ResponseEntity.ok;
import static query.model.user.repository.UserQueryExpressions.usernameMatches;
import static web.common.RequestMappings.PUBLIC_API_SIGN_UP;

import java.util.Optional;

import api.user.command.SendActivationLinkCommand;
import commons.ErrorCode;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import query.model.user.UserEntity;
import query.model.user.UserType;
import web.user.service.CreateUserService;
import web.user.UserBaseController;
import web.user.dto.CreateUserWebCommand;
import web.user.dto.UserDtoFactory;
import web.user.service.CreateUserWebCommandValidator;

@RestController
@Setter(onMethod_ = { @Autowired })
final class SignUpController extends UserBaseController {

    private CreateUserWebCommandValidator validator;
    private CreateUserService createUserService;

    @InitBinder
    private void initValidation(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @PostMapping(PUBLIC_API_SIGN_UP)
    ResponseEntity<?> signUpCustomer(@RequestBody @Validated CreateUserWebCommand createCustomerWebCommand,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return errorResponseService.create(bindingResult);
        }

        createUserService.create(createCustomerWebCommand, UserType.CUSTOMER);
        Optional<UserEntity> customerOptional = userRepository.findOne(
                usernameMatches(createCustomerWebCommand.getUsername()));

        if (customerOptional.isPresent()) {
            UserEntity customer = customerOptional.get();
            commandGateway.sendAndWait(new SendActivationLinkCommand(customer.getId()));
            return ok(UserDtoFactory.create(customer));
        } else {
            return errorResponseService.create("username", ErrorCode.NOT_EXISTS, HttpStatus.CONFLICT);
        }
    }
}
