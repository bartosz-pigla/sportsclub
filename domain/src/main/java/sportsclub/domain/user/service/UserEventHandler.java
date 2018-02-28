package sportsclub.domain.user.service;

import lombok.AllArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import sportsclub.api.user.event.UserActivatedEvent;
import sportsclub.api.user.event.UserCreatedEvent;
import sportsclub.query.user.model.UserEntry;
import sportsclub.query.user.service.UserEntryRepository;

@Component
@AllArgsConstructor
public class UserEventHandler {
    private UserEntryRepository userEntryRepository;

    @EventHandler
    public void on(UserCreatedEvent event) {
        userEntryRepository.save(UserEntry.builder()
                .login(event.getLogin())
                .password(event.getPassword())
                .activated(false)
                .deleted(false)
                .build());
    }

    @EventHandler
    public void on(UserActivatedEvent event) {
        userEntryRepository.activate(event.getLogin());
    }
}
