package query.model.embeddable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import query.exception.ValueObjectCreationException;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Email {

    @Column
    private String email;

    public Email(String email) {
        if (!EmailValidator.getInstance().isValid(email)) {
            throw new ValueObjectCreationException();
        } else {
            this.email = email;
        }
    }
}
