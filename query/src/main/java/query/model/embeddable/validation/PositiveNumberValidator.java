package query.model.embeddable.validation;

import commons.ErrorCode;
import org.springframework.validation.Errors;

public class PositiveNumberValidator {

    public static boolean isInvalid(Integer positiveNumber) {
        return positiveNumber == null || positiveNumber <= 0;
    }

    public static void validate(Integer positiveNumber, Errors errors) {
        if (positiveNumber == null) {
            errors.rejectValue("positiveNumber", ErrorCode.EMPTY.getCode());
        } else if (positiveNumber <= 0) {
            errors.rejectValue("positiveNumber", ErrorCode.NOT_POSITIVE.getCode());
        }
    }
}
