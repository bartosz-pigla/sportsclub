package sportsclub.web.controller;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import sportsclub.api.validation.ValidationException;

@ControllerAdvice
class GlobalControllerExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ValidationException.class)
    public void handleConflict(ValidationException e) {
        LocaleContextHolder.getLocale();
        for (FieldError error : e.getErrors().getFieldErrors()) {
            System.out.println(error.getRejectedValue());
        }
    }
}