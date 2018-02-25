package sportsclub.domain.user.service;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import sportsclub.api.user.event.UserCreatedEvent;

@Component
public class UserEventHandler {

    @EventHandler
    public void on(UserCreatedEvent event) {
        System.out.println("EVENT: " + event);
    }
}
