package query.model.embeddable;

import static query.validation.DateRangeValidator.isInvalid;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import query.exception.ValueObjectCreationException;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateRange {

    @Column
    private LocalDateTime dateFrom;
    @Column
    private LocalDateTime dateTo;

    public DateRange(LocalDateTime dateFrom, LocalDateTime dateTo) {
        if (isInvalid(dateFrom, dateTo)) {
            throw new ValueObjectCreationException();
        } else {
            this.dateFrom = dateFrom;
            this.dateTo = dateTo;
        }
    }
}
