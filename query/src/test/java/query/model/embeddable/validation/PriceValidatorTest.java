package query.model.embeddable.validation;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;
import query.exception.ValueObjectCreationException;
import query.model.embeddable.Price;

public class PriceValidatorTest {

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenPriceIsEmptyOrNull() {
        new Price(null);
    }

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenPriceIsNotPositive() {
        new Price(new BigDecimal(0d));
    }

    @Test
    public void shouldAcceptWhenPriceIsValid() {
        BigDecimal priceBigDecimal = new BigDecimal(12.23);
        Price price = new Price(priceBigDecimal);
        assertEquals(priceBigDecimal.setScale(2, BigDecimal.ROUND_CEILING), price.getPrice());
    }
}
