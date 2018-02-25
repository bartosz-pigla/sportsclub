package sportsclub.api.user.event;

import lombok.Value;

import java.io.Serializable;

@Value
public class UserActivatedEvent implements Serializable {
    private static final long serialVersionUID = 2638013818019363752L;

    String login;
}
