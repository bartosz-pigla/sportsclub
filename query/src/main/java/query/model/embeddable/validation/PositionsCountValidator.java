package query.model.embeddable.validation;

import commons.ErrorCode;
import org.springframework.validation.Errors;

public class PositionsCountValidator {

    public static boolean isInvalid(Integer positionsCount) {
        return positionsCount == null || positionsCount <= 0;
    }

    public static void validate(Integer positionsCount, Errors errors) {
        if (positionsCount == null) {
            errors.rejectValue("positionsCount", ErrorCode.EMPTY.getCode());
        } else if (positionsCount <= 0) {
            errors.rejectValue("positionsCount", ErrorCode.NOT_POSITIVE.getCode());
        }
    }
}
