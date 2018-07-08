package query.model.embeddable;

import static query.model.embeddable.validation.EmailValidator.isInvalid;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import query.exception.ValueObjectCreationException;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Email {

    @Column
    private String email;

    public Email(String email) {
        if (isInvalid(email)) {
            throw new ValueObjectCreationException();
        } else {
            this.email = email;
        }
    }
}
