package query.model.embeddable.validation;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.validator.routines.InetAddressValidator.getInstance;

import org.springframework.validation.Errors;

public class InetAddressValidator {

    public static boolean isInvalid(String inetAddress) {
        return inetAddress == null || !getInstance().isValid(inetAddress);
    }

    public static void validate(String inetAddress, Errors errors) {
        if (isBlank(inetAddress)) {
            errors.rejectValue("inetAddress", "inetAddress.empty");
        }

        if (!getInstance().isValid(inetAddress)) {
            errors.rejectValue("inetAddress", "inetAddress.notValid");
        }
    }
}
