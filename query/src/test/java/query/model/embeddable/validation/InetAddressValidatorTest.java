package query.model.embeddable.validation;

import org.junit.Test;
import query.exception.ValueObjectCreationException;
import query.model.embeddable.InetAddress;

public class InetAddressValidatorTest {

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenInetAddressIsNull() {
        new InetAddress(null);
    }

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenInetAddressIsEmpty() {
        new InetAddress("");
    }

    @Test(expected = ValueObjectCreationException.class)
    public void shouldRejectWhenInetAddressIsInvalid() {
        new InetAddress("192.168.1.a");
    }

    public void shouldAcceptWhenInetAddressIsValid() {
        new InetAddress("192.168.1.1");
    }
}
