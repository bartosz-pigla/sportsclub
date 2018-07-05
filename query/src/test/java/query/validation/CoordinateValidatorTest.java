package query.validation;

import static junit.framework.TestCase.assertEquals;

import org.junit.Test;
import query.exception.ValueObjectCreationException;
import query.model.embeddable.Coordinates;

public class CoordinateValidatorTest {

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenLatitudeIsTooSmall() {
        new Coordinates(-120d, 0d);
    }

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenLatitudeIsTooBig() {
        new Coordinates(120d, 0d);
    }

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenLongitudeIsTooSmall() {
        new Coordinates(0d, -200d);
    }

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenLongitudeIsTooBig() {
        new Coordinates(0d, 200d);
    }

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenLatitudeIsNull() {
        new Coordinates(null, 0d);
    }

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenBothAreInvalid() {
        new Coordinates(-120d, -200d);
    }

    @Test
    public void shouldAcceptWhenBothAreValid() {
        Double latitude = 43.3d;
        Double longitude = 12.3d;
        Coordinates coordinates = new Coordinates(latitude, longitude);
        assertEquals(coordinates.getLatitude(), latitude);
        assertEquals(coordinates.getLongitude(), longitude);
    }
}
