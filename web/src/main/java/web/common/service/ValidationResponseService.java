package web.common.service;

import java.util.stream.Collectors;

import commons.ErrorCode;
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

    public ResponseEntity<?> getResponse(HttpStatus httpStatus, FieldErrorDto... fields) {
        return new ResponseEntity<>(fields, httpStatus);
    }

    public ResponseEntity<?> getOneFieldErrorResponse(String fieldName, ErrorCode errorCode) {
        return getResponse(
                HttpStatus.CONFLICT,
                new FieldErrorDto(fieldName, errorCode.getCode()));
    }
}
