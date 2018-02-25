package sportsclub.domain.user.service;

import org.axonframework.commandhandling.model.AggregateNotFoundException;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import sportsclub.api.user.event.UserCreatedEvent;
import sportsclub.domain.user.model.User;
import sportsclub.domain.user.model.UserEntry;

@Component
public class UserEventHandler {
    private Repository<User> aggregateRepository;
    private UserEntryRepository userEntryRepository;

    public UserEventHandler(Repository<User> aggregateRepository, UserEntryRepository userEntryRepository) {
        this.aggregateRepository = aggregateRepository;
        this.userEntryRepository = userEntryRepository;
    }

    @EventHandler
    public void on(UserCreatedEvent event) {
        userEntryRepository.save(UserEntry.builder()
                .login(event.getLogin())
                .password(event.getPassword())
                .activated(false)
                .deleted(false)
                .build());
    }
}
