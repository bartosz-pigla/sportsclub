package web.signUp;

import static org.apache.commons.lang3.StringUtils.isBlank;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import query.model.embeddable.validation.EmailValidator;
import query.model.embeddable.validation.PhoneNumberValidator;

@Service
class CreateCustomerValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return CreateCustomerCommand.class.equals(aClass);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        CreateCustomerCommand command = (CreateCustomerCommand) obj;
        validateUsername(command.getUsername(), errors);
        validatePassword(command.getPassword(), errors);
        EmailValidator.validate(command.getEmail(), errors);
        PhoneNumberValidator.validate(command.getPhoneNumber(), errors);
    }

    private void validateUsername(String username, Errors errors) {
        if (isBlank(username)) {
            errors.rejectValue("username", "username.empty");
        }
    }

    private void validatePassword(String password, Errors errors) {
        if (isBlank(password)) {
            errors.rejectValue("password", "password.empty");
        }
    }
}
