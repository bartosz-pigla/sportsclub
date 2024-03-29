package domain.user;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;
import static org.springframework.beans.BeanUtils.copyProperties;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import api.user.command.ActivateCustomerCommand;
import api.user.command.ActivateUserCommand;
import api.user.command.CreateUserCommand;
import api.user.command.DeactivateUserCommand;
import api.user.command.DeleteUserCommand;
import api.user.command.SendActivationLinkCommand;
import api.user.event.ActivationLinkSentEvent;
import api.user.event.UserActivatedEvent;
import api.user.event.UserCreatedEvent;
import api.user.event.UserDeactivatedEvent;
import api.user.event.UserDeletedEvent;
import domain.user.activation.customer.service.ActivateCustomerValidator;
import domain.user.activation.customer.service.SendActivationLinkValidator;
import domain.user.activation.user.service.ActivateUserValidator;
import domain.user.activation.user.service.DeactivateUserValidator;
import domain.user.createUser.service.CreateUserValidator;
import domain.user.deleteUser.service.DeleteUserValidator;
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
    private boolean activated;
    private boolean deleted;

    @CommandHandler
    public User(CreateUserCommand command, CreateUserValidator validator) {
        validator.validate(command);
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
    public void sendActivationLink(SendActivationLinkCommand command, SendActivationLinkValidator validator) {
        validator.validate(this);
        apply(new ActivationLinkSentEvent(command.getCustomerId(), UUID.randomUUID(), DateTimeRange.create()));
    }

    @EventSourcingHandler
    public void on(ActivationLinkSentEvent event) {
        activationDeadline = event.getDateTimeRange().getDateTo();
        activationKey = event.getActivationKey();
    }

    @CommandHandler
    public void activateCustomer(ActivateCustomerCommand command, ActivateCustomerValidator validator) {
        validator.validate(command, this);
        apply(new UserActivatedEvent(command.getCustomerId()));
    }

    @CommandHandler
    public void activateUser(ActivateUserCommand command, ActivateUserValidator validator) {
        validator.validate(command, this);
        apply(new UserActivatedEvent(command.getUserId()));
    }

    @EventSourcingHandler
    public void on(UserActivatedEvent event) {
        activated = true;
    }

    @CommandHandler
    public void deactivateUser(DeactivateUserCommand command, DeactivateUserValidator validator) {
        validator.validate(command, this);
        apply(new UserDeactivatedEvent(command.getUserId()));
    }

    @EventSourcingHandler
    public void on(UserDeactivatedEvent event) {
        activated = false;
    }

    @CommandHandler
    public void deleteUser(DeleteUserCommand command, DeleteUserValidator validator) {
        validator.validate(this);
        apply(new UserDeletedEvent(command.getUserId()));
    }

    @EventSourcingHandler
    public void on(UserDeletedEvent event) {
        deleted = true;
    }
}
