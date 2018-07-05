package api.common;

import org.springframework.validation.Errors;

public class DomainValidationException extends RuntimeException {

    private Errors errors;

    public DomainValidationException(Errors errors) {
        this.errors = errors;
    }

    public Errors getErrors() {
        return errors;
    }
}
