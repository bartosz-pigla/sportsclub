package web.adminApi.sportObject.service;

import java.math.BigDecimal;
import java.time.LocalTime;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import query.model.embeddable.validation.OpeningTimeRangeValidator;
import query.model.embeddable.validation.PriceValidator;
import web.adminApi.sportObject.dto.OpeningTimeDto;

@Service
public class OpeningTimeRangeDtoValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return OpeningTimeDto.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        OpeningTimeDto obj = (OpeningTimeDto) target;
        OpeningTimeRangeValidator.validate(
                obj.getDayOfWeek(),
                LocalTime.of(obj.getStartTime().getHour(), obj.getStartTime().getMinute()),
                LocalTime.of(obj.getFinishTime().getHour(), obj.getFinishTime().getMinute()),
                errors);
        PriceValidator.validate(new BigDecimal(obj.getPrice()), errors);
    }
}
