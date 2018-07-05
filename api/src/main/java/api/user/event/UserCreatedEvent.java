package api.user.event;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCreatedEvent implements Serializable {

    private static final long serialVersionUID = -507401947375378352L;

    String login, password;
}
