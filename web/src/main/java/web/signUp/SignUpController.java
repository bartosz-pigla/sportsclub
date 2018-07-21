package web.signUp;

import static web.common.RequestMappings.SIGN_UP;

import api.user.command.CreateUserCommand;
import api.user.command.SendActivationLinkCommand;
import domain.user.createUser.exception.UserCreationException;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import query.model.embeddable.Email;
import query.model.embeddable.PhoneNumber;
import query.model.user.UserType;
import query.repository.UserEntityRepository;
import web.common.BaseController;
import web.common.FieldErrorDto;
import web.signUp.dto.CreateCustomerWebCommand;
import web.signUp.service.CreateCustomerValidator;

@RestController
@Setter(onMethod_ = { @Autowired })
final class SignUpController extends BaseController {

    private CreateCustomerValidator validator;
    private UserEntityRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @InitBinder
    private void initValidation(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @PostMapping(SIGN_UP)
    ResponseEntity<?> signUpCustomer(@RequestBody @Validated CreateCustomerWebCommand customer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validationResponseService.getResponse(bindingResult);
        }

        commandGateway.sendAndWait(CreateUserCommand.builder()
                .username(customer.getUsername())
                .password(passwordEncoder.encode(customer.getPassword()))
                .email(new Email(customer.getEmail()))
                .phoneNumber(new PhoneNumber(customer.getPhoneNumber()))
                .userType(UserType.CUSTOMER).build());

        userRepository.findByUsername(customer.getUsername()).ifPresent(c ->
                commandGateway.sendAndWait(SendActivationLinkCommand.builder().customerId(c.getId()).build()));

        return ResponseEntity.ok(customer);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserCreationException.class)
    public ResponseEntity<?> handleUserAlreadyExistsConflict() {
        return validationResponseService.getResponse(
                HttpStatus.CONFLICT,
                new FieldErrorDto("username", "alreadyExists"));
    }
}
