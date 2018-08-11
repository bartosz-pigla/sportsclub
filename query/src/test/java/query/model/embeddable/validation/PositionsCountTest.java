package query.model.embeddable.validation;

import org.junit.Test;
import query.exception.ValueObjectCreationException;
import query.model.embeddable.PositionsCount;

public class PositionsCountTest {

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenNumberIsEmpty() {
        new PositionsCount(null);
    }

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenNumberIsZero() {
        new PositionsCount(0);
    }

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenNumberIsNegative() {
        new PositionsCount(-1);
    }

    public void shouldAcceptWhenNumberIsPositive() {
        new PositionsCount(1);
    }
}
