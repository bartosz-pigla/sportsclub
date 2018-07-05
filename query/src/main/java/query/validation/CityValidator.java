package query.validation;

import static org.apache.commons.lang3.StringUtils.isAlpha;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static query.validation.ValidationHelper.hasInvalidLength;

import org.springframework.validation.Errors;

public class CityValidator {

    private static final int CITY_MAX_LENGTH = 30;

    public static boolean isInvalid(String city) {
        return isBlank(city) || hasInvalidLength(city, 0, CITY_MAX_LENGTH) || !isAlpha(city);
    }

    public static void validate(String city, Errors errors) {
        if (isBlank(city)) {
            errors.rejectValue("city", "city.empty");
        }

        if (hasInvalidLength(city, 0, CITY_MAX_LENGTH)) {
            errors.rejectValue("city", "city.maxLength");
        }

        if (!isAlpha(city)) {
            errors.rejectValue("city", "city.isNotAlpha");
        }
    }
}
