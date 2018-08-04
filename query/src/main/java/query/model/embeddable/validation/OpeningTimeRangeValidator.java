package query.model.embeddable.validation;

import static org.apache.commons.lang3.ObjectUtils.allNotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;

import commons.ErrorCode;
import org.springframework.validation.Errors;

public class OpeningTimeRangeValidator {

    public static boolean isInvalid(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime finishTime) {
        return !allNotNull(dayOfWeek, startTime, finishTime) || !startTime.isBefore(finishTime);
    }

    public static void validate(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime finishTime, Errors errors) {
        if (dayOfWeek == null) {
            errors.rejectValue("dateRange", ErrorCode.EMPTY.getCodeWithPrefix("dayOfWeek"));
        }

        if (startTime == null) {
            errors.rejectValue("dateRange", ErrorCode.EMPTY.getCodeWithPrefix("openingTime"));
        }

        if (finishTime == null) {
            errors.rejectValue("dateRange", ErrorCode.EMPTY.getCodeWithPrefix("finishTime"));
        }

        if (startTime != null && finishTime != null && !startTime.isBefore(finishTime)) {
            errors.rejectValue("dateRange", ErrorCode.INVALID.getCode());
        }
    }
}
