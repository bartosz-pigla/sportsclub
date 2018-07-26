package query.model.embeddable.validation;

import static org.apache.commons.lang3.StringUtils.isAlpha;
import static org.apache.commons.lang3.StringUtils.isBlank;

import commons.ErrorCode;
import org.springframework.validation.Errors;

public class CityValidator {

    private static final int CITY_MAX_LENGTH = 30;

    public static boolean isInvalid(String city) {
        return isBlank(city) || ValidationHelper.hasInvalidLength(city, 0, CITY_MAX_LENGTH) || !isAlpha(city);
    }

    public static void validate(String city, Errors errors) {
        if (isBlank(city)) {
            errors.rejectValue("city", ErrorCode.EMPTY.getCode());
        } else {
            if (ValidationHelper.hasInvalidLength(city, 0, CITY_MAX_LENGTH)) {
                errors.rejectValue("city", ErrorCode.MAX_LENGTH.getCode());
            }

            if (!isAlpha(city)) {
                errors.rejectValue("city", ErrorCode.IS_NOT_ALPHA.getCode());
            }
        }
    }
}
