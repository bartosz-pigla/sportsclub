package sportsclub.domain.user.model;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.validation.Errors;
import sportsclub.api.user.command.ActivateUserCommand;
import sportsclub.api.user.command.CreateUserCommand;
import sportsclub.api.user.event.UserActivatedEvent;
import sportsclub.api.user.event.UserCreatedEvent;
import sportsclub.domain.user.service.UserValidator;

import java.io.Serializable;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate
public class User implements Serializable {

    private static final long serialVersionUID = -9081727511679911242L;

    @AggregateIdentifier
    private String login;

    private String password;

    private boolean activated;

    public User() {
    }

    @CommandHandler
    public User(CreateUserCommand command, UserValidator validator, Errors errors) {
        validator.validate(command, errors);
        if (!errors.hasErrors()) {
            apply(new UserCreatedEvent(command.getLogin(), command.getPassword()));
        }
    }

    @EventSourcingHandler
    public void on(UserCreatedEvent event) {
        login = event.getLogin();
        password = event.getPassword();
    }

    @CommandHandler
    public void on(ActivateUserCommand command, UserValidator validator, Errors errors) {
        validator.validate(command, errors);
        if (!errors.hasErrors()) {
            activated = true;
            apply(new UserActivatedEvent(command.getLogin()));
        }
    }
}
