package query.model.embeddable;

import static query.model.embeddable.validation.PositionsCountValidator.isInvalid;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import query.exception.ValueObjectCreationException;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PositionsCount {

    @Column
    private Integer positionsCount;

    public PositionsCount(Integer positionsCount) {
        if (isInvalid(positionsCount)) {
            throw new ValueObjectCreationException();
        } else {
            this.positionsCount = positionsCount;
        }
    }
}
