package web.common.service;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import commons.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import web.common.dto.FieldErrorDto;

@Service
public class ErrorResponseService {

    public ResponseEntity<?> create(BindingResult bindingResult) {
        return new ResponseEntity<>(bindingResult.getFieldErrors().stream()
                .map(field -> new FieldErrorDto(field.getField(), field.getCode()))
                .collect(Collectors.toList()), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> create(String field, ErrorCode code, HttpStatus status) {
        return new ResponseEntity<>(createBody(field, code), status);
    }

    public List<FieldErrorDto> createBody(String field, ErrorCode code) {
        return ImmutableList.of(new FieldErrorDto(field, code));
    }
}
