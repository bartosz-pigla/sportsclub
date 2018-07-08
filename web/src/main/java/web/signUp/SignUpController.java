package web.signUp;

import static web.common.RequestMappings.SIGN_UP;

import api.user.command.CreateUserCommand;
import domain.user.UserCreationException;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import web.common.BaseController;
import web.common.FieldErrorDto;
import web.common.ValidationResponseService;

@RestController
final class SignUpController extends BaseController {

    private CreateCustomerValidator validator;

    SignUpController(CommandGateway commandGateway,
                     ValidationResponseService validationResponseService,
                     CreateCustomerValidator validator) {
        super(commandGateway, validationResponseService);
        this.validator = validator;
    }

    @InitBinder
    private void initValidation(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @PostMapping(SIGN_UP)
    ResponseEntity<?> signUpCustomer(@RequestBody @Validated CreateCustomerCommand customer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validationResponseService.getResponse(bindingResult);
        }

        commandGateway.sendAndWait(CreateUserCommand.builder()
                .username(customer.getUsername())
                .password(customer.getPassword())
                .email(new Email(customer.getEmail()))
                .phoneNumber(new PhoneNumber(customer.getPhoneNumber()))
                .userType(UserType.CUSTOMER).build());

        return ResponseEntity.ok(customer);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserCreationException.class)
    public ResponseEntity<?> handleUserAlreadyExistsConflict(UserCreationException e) {
        return validationResponseService.getResponse(
                HttpStatus.CONFLICT,
                new FieldErrorDto("username", "alreadyExists"),
                new FieldErrorDto("username", e.getCommand().getUsername()));
    }
}
