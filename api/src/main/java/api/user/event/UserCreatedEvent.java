package api.user.event;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import query.model.embeddable.Email;
import query.model.embeddable.PhoneNumber;
import query.model.user.UserType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class UserCreatedEvent extends DomainEvent implements Serializable {

    private static final long serialVersionUID = -507401947375378352L;

    private UUID userId;
    private String username;
    private String password;
    private UserType userType;
    private Email email;
    private PhoneNumber phoneNumber;
}
