package domain.user;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;
import static org.springframework.beans.BeanUtils.copyProperties;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import api.user.command.CreateUserCommand;
import api.user.command.SendActivationLinkCommand;
import api.user.event.ActivationLinkSentEvent;
import api.user.event.UserCreatedEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import query.model.embeddable.DateTimeRange;
import query.model.user.UserType;
import query.repository.UserEntityRepository;

@Aggregate
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class User implements Serializable {

    private static final long serialVersionUID = -597312647396081757L;

    @AggregateIdentifier
    private UUID userId;
    private UserType userType;
    private LocalDateTime activationDeadline;
    private UUID activationKey;

    private UserValidator userValidator;
    private UserEntityRepository userRepository;

    @CommandHandler
    public User(CreateUserCommand command, UserValidator userValidator, UserEntityRepository userRepository) {
        this.userValidator = userValidator;
        this.userRepository = userRepository;
        this.userValidator.validate(command);

        UserCreatedEvent event = new UserCreatedEvent();
        copyProperties(command, event);
        event.setUserId(UUID.randomUUID());
        apply(event);
    }

    @EventSourcingHandler
    public void on(UserCreatedEvent event) {
        copyProperties(event, this);
    }

    @CommandHandler
    public void sendActivationLink(SendActivationLinkCommand command) {
        apply(new ActivationLinkSentEvent(command.getCustomerId(), UUID.randomUUID(), DateTimeRange.create()));
    }

    @EventSourcingHandler
    public void on(ActivationLinkSentEvent event) {
        this.activationDeadline = event.getDateTimeRange().getDateTo();
        this.activationKey = event.getActivationKey();
    }
}
