package query.model.embeddable.validation;

import org.junit.Test;
import query.exception.ValueObjectCreationException;
import query.model.embeddable.PositiveNumber;

public class PositiveNumberTest {

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenNumberIsEmpty() {
        new PositiveNumber(null);
    }

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenNumberIsZero() {
        new PositiveNumber(0);
    }

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenNumberIsNegative() {
        new PositiveNumber(-1);
    }

    public void shouldAcceptWhenNumberIsPositive() {
        new PositiveNumber(1);
    }
}
