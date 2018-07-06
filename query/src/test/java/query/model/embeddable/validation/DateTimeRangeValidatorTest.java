package query.model.embeddable.validation;

import java.time.LocalDateTime;

import org.junit.Test;
import query.exception.ValueObjectCreationException;
import query.model.embeddable.DateTimeRange;

public class DateTimeRangeValidatorTest {

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenDateFromIsNull() {
        new DateTimeRange(null, LocalDateTime.now().plusDays(1));
    }

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenDateToIsNull() {
        new DateTimeRange(LocalDateTime.now(), null);
    }

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenBothDatesAreNull() {
        new DateTimeRange(null, null);
    }

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenDateToIsBeforeDateFrom() {
        new DateTimeRange(LocalDateTime.now().plusDays(1), LocalDateTime.now());
    }

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenBothDatesAreEqual() {
        LocalDateTime date = LocalDateTime.now();
        new DateTimeRange(date, date);
    }

    @Test
    public void shouldAcceptWhenDateRangeIsValid() {
        new DateTimeRange(LocalDateTime.now(), LocalDateTime.now().plusHours(1));
    }
}
