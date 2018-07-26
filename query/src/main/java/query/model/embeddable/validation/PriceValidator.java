package query.model.embeddable.validation;

import java.math.BigDecimal;

import commons.ErrorCode;
import org.springframework.validation.Errors;

public class PriceValidator {

    public static boolean isInvalid(BigDecimal price) {
        return price == null || isNegative(price);
    }

    public static void validate(BigDecimal price, Errors errors) {
        if (price == null) {
            errors.rejectValue("price", ErrorCode.EMPTY.getCode());
        } else if (isNegative(price)) {
            errors.rejectValue("price", ErrorCode.NOT_POSITIVE.getCode());
        }
    }

    private static boolean isNegative(BigDecimal price) {
        return price.compareTo(new BigDecimal(0)) <= 0;
    }
}
