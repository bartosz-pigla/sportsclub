package api.user.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import query.model.embeddable.Email;
import query.model.embeddable.PhoneNumber;
import query.model.user.UserType;

@Data
@AllArgsConstructor
@Builder
public class CreateUserCommand {

    private String username;
    private String password;
    private UserType userType;
    private Email email;
    private PhoneNumber phoneNumber;
}
