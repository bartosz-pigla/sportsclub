package query.model.embeddable.validation;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.validator.routines.EmailValidator.getInstance;

import org.springframework.validation.Errors;

public class EmailValidator {

    public static boolean isInvalid(String email) {
        return !getInstance().isValid(email);
    }

    public static void validate(String email, Errors errors) {
        if (isBlank(email)) {
            errors.rejectValue("email", "email.empty");
        } else if (!getInstance().isValid(email)) {
            errors.rejectValue("email", "email.invalid");
        }
    }
}
