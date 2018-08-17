package web.common.service;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import web.common.dto.FieldErrorDto;

@Service
public class ValidationResponseService {

    public ResponseEntity<?> getResponse(BindingResult bindingResult) {
        return new ResponseEntity<>(bindingResult.getFieldErrors().stream()
                .map(field -> new FieldErrorDto(field.getField(), field.getCode()))
                .collect(Collectors.toList()), HttpStatus.BAD_REQUEST);
    }
}
