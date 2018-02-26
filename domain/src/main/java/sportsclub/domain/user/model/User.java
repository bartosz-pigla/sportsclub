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
import sportsclub.api.validation.ValidationException;
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
    public User(CreateUserCommand command, UserValidator validator) {
        Errors errors = validator.validate(command);
        if (errors == null) {
            apply(new UserCreatedEvent(command.getLogin(), command.getPassword()));
        } else {
            throw new ValidationException(errors);
        }
    }

    @EventSourcingHandler
    public void on(UserCreatedEvent event) {
        login = event.getLogin();
        password = event.getPassword();
    }

    @CommandHandler
    public void on(ActivateUserCommand command, UserValidator validator) {
        Errors errors = validator.validate(command);
        if (errors == null) {
            apply(new UserActivatedEvent(command.getLogin()));
        } else {
            throw new ValidationException(errors);
        }
    }

    @EventSourcingHandler
    public void on(UserActivatedEvent event) {
        activated = true;
    }

}
