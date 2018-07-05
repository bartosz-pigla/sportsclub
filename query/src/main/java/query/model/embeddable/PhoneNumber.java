package query.model.embeddable;

import static query.validation.PhoneNumberValidator.isInvalid;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import query.exception.ValueObjectCreationException;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PhoneNumber {

    @Column
    private String phoneNumber;

    public PhoneNumber(String phoneNumber) {
        if (isInvalid(phoneNumber)) {
            throw new ValueObjectCreationException();
        } else {
            this.phoneNumber = phoneNumber;
        }
    }
}
