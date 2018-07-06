package query.model.embeddable;

import static query.model.embeddable.validation.PositiveNumberValidator.isInvalid;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import query.exception.ValueObjectCreationException;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PositiveNumber {

    @Column
    private Integer positiveNumber;

    public PositiveNumber(Integer positiveNumber) {
        if (isInvalid(positiveNumber)) {
            throw new ValueObjectCreationException();
        } else {
            this.positiveNumber = positiveNumber;
        }
    }
}
