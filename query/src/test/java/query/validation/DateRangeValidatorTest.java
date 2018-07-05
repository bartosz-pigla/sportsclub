package query.validation;

import java.time.LocalDateTime;

import org.junit.Test;
import query.exception.ValueObjectCreationException;
import query.model.embeddable.DateRange;

public class DateRangeValidatorTest {

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenDateFromIsNull() {
        new DateRange(null, LocalDateTime.now().plusDays(1));
    }

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenDateToIsNull() {
        new DateRange(LocalDateTime.now(), null);
    }

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenBothDatesAreNull() {
        new DateRange(null, null);
    }

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenDateToIsBeforeDateFrom() {
        new DateRange(LocalDateTime.now().plusDays(1), LocalDateTime.now());
    }

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenBothDatesAreEqual() {
        LocalDateTime date = LocalDateTime.now();
        new DateRange(date, date);
    }

    @Test
    public void shouldAcceptWhenDateRangeIsValid() {
        new DateRange(LocalDateTime.now(), LocalDateTime.now().plusHours(1));
    }
}
