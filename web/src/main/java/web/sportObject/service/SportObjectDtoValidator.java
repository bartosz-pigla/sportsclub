package web.sportObject.service;

import static org.apache.commons.lang3.StringUtils.isBlank;

import commons.ErrorCode;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import query.model.embeddable.validation.CoordinatesValidator;
import web.sportObject.dto.AddressDto;
import web.sportObject.dto.SportObjectDto;

@Service
public class SportObjectDtoValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return SportObjectDto.class.equals(aClass);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        SportObjectDto dto = (SportObjectDto) obj;
        validateName(dto.getName(), errors);
        validateImageUrl(dto.getImageUrl(), errors);
        validateAddress(dto.getAddress(), errors);
    }

    private void validateName(String name, Errors errors) {
        if (isBlank(name)) {
            errors.rejectValue("name", ErrorCode.EMPTY.getCode());
        }
    }

    private void validateImageUrl(String imageUrl, Errors errors) {
        if (isBlank(imageUrl)) {
            errors.rejectValue("imageUrl", ErrorCode.EMPTY.getCode());
        } else if (!UrlValidator.getInstance().isValid(imageUrl)) {
            errors.rejectValue("imageUrl", ErrorCode.INVALID.getCode());
        }
    }

    private void validateAddress(AddressDto address, Errors errors) {
//        CityValidator.validate(address.getCity(), errors);
        CoordinatesValidator.validate(address.getLatitude(), address.getLongitude(), errors);
    }
}
