package sportsclub.api.user.event;

import lombok.Value;

import java.io.Serializable;

@Value
public class UserCreatedEvent implements Serializable {
    private static final long serialVersionUID = -507401947375378352L;

    String login;
    String password;
}
