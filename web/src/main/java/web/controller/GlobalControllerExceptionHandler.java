package web.controller;

import api.common.DomainValidationException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class GlobalControllerExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DomainValidationException.class)
    public void handleConflict(DomainValidationException e) {
        LocaleContextHolder.getLocale();
        for (FieldError error : e.getErrors().getFieldErrors()) {
            System.out.println(error.getRejectedValue());
        }
    }
}
