package web.sportObject.service;

import static org.apache.commons.lang3.StringUtils.isBlank;

import commons.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import query.model.embeddable.validation.PositionsCountValidator;
import web.sportObject.dto.SportObjectPositionDto;

@Service
public class SportObjectPositionDtoValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return SportObjectPositionDto.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        SportObjectPositionDto dto = (SportObjectPositionDto) o;
        validateName(dto.getName(), errors);
        PositionsCountValidator.validate(dto.getPositionsCount(), errors);
    }

    private void validateName(String name, Errors errors) {
        if (isBlank(name)) {
            errors.rejectValue("name", ErrorCode.EMPTY.getCode());
        }
    }
}
