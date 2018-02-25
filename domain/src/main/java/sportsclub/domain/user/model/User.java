package sportsclub.domain.user.model;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import sportsclub.api.user.CreateUserCommand;
import sportsclub.api.user.UserCreatedEvent;

import java.io.Serializable;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate
public class User implements Serializable {

    private static final long serialVersionUID = -9081727511679911242L;

    @AggregateIdentifier
    private String login;

    private String password;

    public User() {
    }

    @CommandHandler
    public User(CreateUserCommand command) {
        login = command.getLogin();
        password = command.getPassword();
        apply(new UserCreatedEvent(login, password));
    }
}
