package sportsclub.api.validation;

import lombok.Getter;
import org.springframework.validation.Errors;

public class ValidationException extends RuntimeException {
    @Getter
    private Errors errors;

    public ValidationException(Errors errors) {
        super();
        this.errors = errors;
    }
}
