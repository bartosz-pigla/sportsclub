package sportsclub.api.user.event;

import lombok.Value;

@Value
public class UserCreatedEvent {
    String login;
    String password;
}
