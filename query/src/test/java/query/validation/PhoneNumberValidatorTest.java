package query.validation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import query.exception.ValueObjectCreationException;
import query.model.embeddable.PhoneNumber;

public class PhoneNumberValidatorTest {

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenPhoneNumberHasWrongLength() {
        new PhoneNumber("+483232");
    }

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenAreaCodeNotStartsWithPlusChar() {
        new PhoneNumber("23232");
    }

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenAreaCodeContainsLetters() {
        new PhoneNumber("+4a2323");
    }

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenLineNumberIsNotNumber() {
        new PhoneNumber("+48a23456789");
    }

    @Test
    public void shouldAcceptWhenPhoneNumberIsValid() {
        String phoneNumberStr = "+48123456789";
        PhoneNumber phoneNumber = new PhoneNumber(phoneNumberStr);
        assertEquals(phoneNumberStr, phoneNumber.getPhoneNumber());
    }
}
