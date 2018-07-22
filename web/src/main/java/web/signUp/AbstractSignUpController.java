package web.signUp;

import api.user.command.CreateUserCommand;
import domain.user.createUser.exception.UserCreationException;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseStatus;
import query.model.embeddable.Email;
import query.model.embeddable.PhoneNumber;
import query.model.user.UserType;
import query.repository.UserEntityRepository;
import web.common.BaseController;
import web.common.FieldErrorDto;
import web.signUp.dto.CreateUserWebCommand;
import web.signUp.service.CreateUserWebCommandValidator;

@Setter(onMethod_ = { @Autowired })
abstract class AbstractSignUpController extends BaseController {

    protected CreateUserWebCommandValidator validator;
    protected UserEntityRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @InitBinder
    private void initValidation(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    protected void signUpUser(CreateUserWebCommand user, UserType userType) {
        commandGateway.sendAndWait(CreateUserCommand.builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .email(new Email(user.getEmail()))
                .phoneNumber(new PhoneNumber(user.getPhoneNumber()))
                .userType(userType).build());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserCreationException.class)
    public ResponseEntity<?> handleUserAlreadyExistsConflict() {
        return validationResponseService.getResponse(
                HttpStatus.CONFLICT,
                new FieldErrorDto("username", "alreadyExists"));
    }
}
