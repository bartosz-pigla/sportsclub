package domain.user;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;
import static org.springframework.beans.BeanUtils.copyProperties;

import java.io.Serializable;
import java.util.UUID;

import api.user.command.CreateUserCommand;
import api.user.event.UserCreatedEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import query.model.embeddable.Email;
import query.model.embeddable.PhoneNumber;
import query.model.user.UserType;

@Aggregate
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class User implements Serializable {

    private static final long serialVersionUID = -597312647396081757L;

    @AggregateIdentifier
    private UUID id;
    private String username;
    private String password;
    private UserType userType;
    private Email email;
    private PhoneNumber phoneNumber;
    private boolean activated;

    private UserValidator userValidator;

    @CommandHandler
    public User(CreateUserCommand command, UserValidator userValidator) {
        this.userValidator = userValidator;
        this.userValidator.validate(command);

        UserCreatedEvent event = new UserCreatedEvent();
        event.setId(UUID.randomUUID());
        copyProperties(command, event);
        apply(event);
    }

    @EventSourcingHandler
    public void on(UserCreatedEvent event) {
        copyProperties(event, this);
    }
}
