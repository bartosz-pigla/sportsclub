package sportsclub.api.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.Errors;

@AllArgsConstructor
public class ValidationException extends RuntimeException {
    @Getter
    private Errors errors;

//    public ValidationException(Errors errors) {
//        super();
//        this.errors = errors;
//    }
}
