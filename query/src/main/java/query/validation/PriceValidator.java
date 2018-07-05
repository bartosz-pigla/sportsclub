package query.validation;

import java.math.BigDecimal;

import org.springframework.validation.Errors;

public class PriceValidator {

    public static boolean isInvalid(BigDecimal price) {
        return price == null || isNegative(price);
    }

    public static void validate(BigDecimal price, Errors errors) {
        if (price == null) {
            errors.rejectValue("price", "price.empty");
        } else if (isNegative(price)) {
            errors.rejectValue("price", "price.notPositive");
        }
    }

    private static boolean isNegative(BigDecimal price) {
        return price.compareTo(new BigDecimal(0)) <= 0;
    }
}
