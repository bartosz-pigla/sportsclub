package query.model.embeddable.validation;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import query.exception.ValueObjectCreationException;
import query.model.embeddable.City;

public class CityValidatorTest {

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenCityContainsNumbers() {
        new City("City123");
    }

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenCityIsTooLong() {
        new City(StringUtils.repeat('c', 31));
    }

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenCityIsEmpty() {
        new City("");
    }

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenCityIsNull() {
        new City(null);
    }

    @Test
    public void shouldAcceptWhenCityIsValid() {
        String cityStr = "Warsaw";
        City city = new City("Warsaw");
        assertEquals(cityStr, city.getCity());
    }

}
