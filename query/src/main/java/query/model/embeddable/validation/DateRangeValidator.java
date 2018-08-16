package query.model.embeddable.validation;

import java.time.LocalDateTime;

import commons.ErrorCode;
import org.springframework.validation.Errors;

public class DateRangeValidator {

    public static boolean isInvalid(LocalDateTime dateFrom, LocalDateTime dateTo) {
        return dateFrom == null || dateTo == null || !dateTo.isAfter(dateFrom);
    }

    public static void validate(LocalDateTime dateFrom, LocalDateTime dateTo, Errors errors) {
        if (dateFrom == null) {
            errors.rejectValue("timeRange", ErrorCode.EMPTY.getCode());
        }

        if (dateTo == null) {
            errors.rejectValue("timeRange", ErrorCode.EMPTY.getCodeWithPrefix("dateFrom"));
        }

        if (dateFrom != null && dateTo != null && !dateTo.isAfter(dateFrom)) {
            errors.rejectValue("timeRange", ErrorCode.EMPTY.getCodeWithPrefix("dateTo"));
        }
    }
}
