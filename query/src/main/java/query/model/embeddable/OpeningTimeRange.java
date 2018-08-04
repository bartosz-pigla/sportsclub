package query.model.embeddable;

import static query.model.embeddable.validation.OpeningTimeRangeValidator.isInvalid;

import java.time.DayOfWeek;
import java.time.LocalTime;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import query.exception.ValueObjectCreationException;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OpeningTimeRange {

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime finishTime;

    public OpeningTimeRange(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime finishTime) {
        if (isInvalid(dayOfWeek, startTime, finishTime)) {
            throw new ValueObjectCreationException();
        } else {
            this.dayOfWeek = dayOfWeek;
            this.startTime = startTime;
            this.finishTime = finishTime;
        }
    }

    public boolean contains(OpeningTimeRange range) {
        if (this.dayOfWeek == range.getDayOfWeek()) {
            return isInRange(range.getStartTime()) || isInRange(range.getFinishTime());
        } else {
            return false;
        }
    }

    private boolean isInRange(LocalTime time) {
        return time.isAfter(startTime) && time.isBefore(finishTime);
    }
}
