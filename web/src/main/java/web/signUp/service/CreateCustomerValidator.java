package web.signUp.service;

import static org.apache.commons.lang3.StringUtils.isBlank;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import query.model.embeddable.validation.EmailValidator;
import query.model.embeddable.validation.PhoneNumberValidator;
import web.signUp.dto.CreateCustomerWebCommand;

@Service
public class CreateCustomerValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return CreateCustomerWebCommand.class.equals(aClass);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        CreateCustomerWebCommand command = (CreateCustomerWebCommand) obj;
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
