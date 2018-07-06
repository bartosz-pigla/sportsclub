package query.model.embeddable.validation;

import java.time.LocalDateTime;

import org.springframework.validation.Errors;

public class DateRangeValidator {

    public static boolean isInvalid(LocalDateTime dateFrom, LocalDateTime dateTo) {
        return dateFrom == null || dateTo == null || !dateTo.isAfter(dateFrom);
    }

    public static void validate(LocalDateTime dateFrom, LocalDateTime dateTo, Errors errors) {
        if (dateFrom == null) {
            errors.rejectValue("dateRange", "dateRange.dateFrom.empty");
        }

        if (dateTo == null) {
            errors.rejectValue("dateRange", "dateRange.dateTo.empty");
        }

        if (dateFrom != null && dateTo != null && !dateTo.isAfter(dateFrom)) {
            errors.rejectValue("dateRange", "dateRange.invalid");
        }
    }
}
