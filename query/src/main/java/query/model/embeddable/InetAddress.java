package query.model.embeddable;

import static query.model.embeddable.validation.InetAddressValidator.isInvalid;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import query.exception.ValueObjectCreationException;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InetAddress {

    @Column
    private String inetAddress;

    public InetAddress(String inetAddress) {
        if (isInvalid(inetAddress)) {
            throw new ValueObjectCreationException();
        } else {
            this.inetAddress = inetAddress;
        }
    }
}
