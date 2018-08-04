package query.model.embeddable.validation;

import java.time.DayOfWeek;
import java.time.LocalTime;

import org.junit.Test;
import query.exception.ValueObjectCreationException;
import query.model.embeddable.OpeningTimeRange;

public class OpeningTimeRangeValidatorTest {

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenDayOfWeekIsNull() {
        new OpeningTimeRange(null, LocalTime.of(11, 0), LocalTime.of(12, 0));
    }

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenStartTimeIsNull() {
        new OpeningTimeRange(DayOfWeek.MONDAY, null, LocalTime.of(12, 0));
    }

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenFinishTimeIsNull() {
        new OpeningTimeRange(DayOfWeek.MONDAY, LocalTime.of(11, 0), null);
    }

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenStartTimeIsAfterFinishTime() {
        new OpeningTimeRange(DayOfWeek.MONDAY, LocalTime.of(11, 0), LocalTime.of(10, 0));
    }

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenStartTimeIsEqualFinishTime() {
        new OpeningTimeRange(DayOfWeek.MONDAY, LocalTime.of(11, 0), LocalTime.of(11, 0));
    }

    @Test
    public void shouldAcceptWhenOpeningTimeRangeIsValid() {
        new OpeningTimeRange(DayOfWeek.MONDAY, LocalTime.of(11, 0), LocalTime.of(11, 1));
    }
}
