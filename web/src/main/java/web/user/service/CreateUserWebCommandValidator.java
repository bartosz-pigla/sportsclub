package web.user.service;

import static org.apache.commons.lang3.StringUtils.isBlank;

import commons.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import query.model.embeddable.validation.EmailValidator;
import query.model.embeddable.validation.PhoneNumberValidator;
import web.user.dto.CreateUserWebCommand;

@Service
public class CreateUserWebCommandValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return CreateUserWebCommand.class.equals(aClass);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        CreateUserWebCommand command = (CreateUserWebCommand) obj;
        validateUsername(command.getUsername(), errors);
        validatePassword(command.getPassword(), errors);
        EmailValidator.validate(command.getEmail(), errors);
        PhoneNumberValidator.validate(command.getPhoneNumber(), errors);
    }

    private void validateUsername(String username, Errors errors) {
        if (isBlank(username)) {
            errors.rejectValue("username", ErrorCode.EMPTY.getCode());
        }
    }

    private void validatePassword(String password, Errors errors) {
        if (isBlank(password)) {
            errors.rejectValue("password", ErrorCode.EMPTY.getCode());
        }
    }
}
