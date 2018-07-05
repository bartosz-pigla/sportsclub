package query.validation;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNumeric;

import org.springframework.validation.Errors;

public class PhoneNumberValidator {

    private static final int PHONE_NUMBER_MAX_LENGTH = 12;
    private static final int AREA_CODE_MAX_LENGTH = 3;

    public static boolean isInvalid(String phoneNumber) {
        return isBlank(phoneNumber) ||
                phoneNumber.length() != PHONE_NUMBER_MAX_LENGTH ||
                areaCodeIsInvalid(phoneNumber) ||
                lineNumberIsInvalid(phoneNumber);
    }

    public static void validate(String phoneNumber, Errors errors) {
        if (isBlank(phoneNumber)) {
            errors.rejectValue("phoneNumber", "phoneNumber.empty");
        }

        if (phoneNumber.length() != PHONE_NUMBER_MAX_LENGTH) {
            errors.rejectValue("phoneNumber", "phoneNumber.maxLength");
        }

        if (areaCodeIsInvalid(phoneNumber)) {
            errors.rejectValue("phoneNumber", "phoneNumber.areaCode.notValid");
        }

        if (lineNumberIsInvalid(phoneNumber)) {
            errors.rejectValue("phoneNumber", "phoneNumber.lineNumber.notValid");
        }
    }

    private static boolean areaCodeIsInvalid(String phoneNumber) {
        String areaCode = phoneNumber.substring(0, AREA_CODE_MAX_LENGTH);
        return areaCode.charAt(0) != '+' || !isNumeric(areaCode.substring(1));
    }

    private static boolean lineNumberIsInvalid(String phoneNumber) {
        String lineNumber = phoneNumber.substring(AREA_CODE_MAX_LENGTH - 1);
        return !isNumeric(lineNumber);
    }
}
