package domain.user;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.matches;
import static org.axonframework.test.matchers.Matchers.sequenceOf;

import java.time.LocalDateTime;
import java.util.UUID;

import api.user.command.ActivateCustomerCommand;
import api.user.event.ActivationLinkSentEvent;
import api.user.event.UserActivatedEvent;
import domain.common.exception.AlreadyDeletedException;
import domain.user.activation.common.exception.AlreadyActivatedException;
import domain.user.activation.customer.exception.ActivationLinkExpiredException;
import domain.user.activation.customer.exception.ActivationLinkInvalidException;
import org.junit.Test;
import query.model.embeddable.DateTimeRange;

public final class ActivateCustomerTest extends AbstractUserTest {

    @Test
    public void shouldNotActivateCustomerWhenActivationDeadlineExpired() {
        ActivationLinkSentEvent activationLinkSentEvent = getActivationLinkSentEvent(userCreatedEvent.getUserId());
        LocalDateTime now = LocalDateTime.now();
        activationLinkSentEvent.setDateTimeRange(new DateTimeRange(now.minusDays(1), now));

        ActivateCustomerCommand command = ActivateCustomerCommand.builder()
                .customerId(activationLinkSentEvent.getCustomerId())
                .activationKey(activationLinkSentEvent.getActivationKey()).build();

        testFixture.given(userCreatedEvent, activationLinkSentEvent)
                .when(command)
                .expectNoEvents()
                .expectException(ActivationLinkExpiredException.class);
    }

    @Test
    public void shouldNotActivateCustomerWhenActivationKeyIsInvalid() {
        ActivationLinkSentEvent activationLinkSentEvent = getActivationLinkSentEvent(userCreatedEvent.getUserId());
        ActivateCustomerCommand command = ActivateCustomerCommand.builder()
                .customerId(activationLinkSentEvent.getCustomerId())
                .activationKey(UUID.randomUUID()).build();

        testFixture.given(userCreatedEvent, activationLinkSentEvent)
                .when(command)
                .expectNoEvents()
                .expectException(ActivationLinkInvalidException.class);
    }

    @Test
    public void shouldNotActivateCustomerWhenCustomerIsAlreadyActivated() {
        ActivationLinkSentEvent activationLinkSentEvent = getActivationLinkSentEvent(userCreatedEvent.getUserId());
        ActivateCustomerCommand command = ActivateCustomerCommand.builder()
                .customerId(activationLinkSentEvent.getCustomerId())
                .activationKey(activationLinkSentEvent.getActivationKey()).build();
        UserActivatedEvent customerActivatedEvent = UserActivatedEvent.builder()
                .userId(command.getCustomerId()).build();

        testFixture.given(userCreatedEvent, activationLinkSentEvent, customerActivatedEvent)
                .when(command)
                .expectNoEvents()
                .expectException(AlreadyActivatedException.class);
    }

    @Test
    public void shouldNotActivateCustomerWhenCustomerIsDeleted() {
        ActivationLinkSentEvent activationLinkSentEvent = getActivationLinkSentEvent(userCreatedEvent.getUserId());
        ActivateCustomerCommand command = ActivateCustomerCommand.builder()
                .customerId(activationLinkSentEvent.getCustomerId())
                .activationKey(activationLinkSentEvent.getActivationKey()).build();

        testFixture.given(userCreatedEvent, activationLinkSentEvent, userDeletedEvent)
                .when(command)
                .expectNoEvents()
                .expectException(AlreadyDeletedException.class);
    }

    @Test
    public void shouldActivateCustomerWhenActivationKeyIsValidAndDeadlineIsNotExpired() {
        ActivationLinkSentEvent activationLinkSentEvent = getActivationLinkSentEvent(userCreatedEvent.getUserId());
        ActivateCustomerCommand command = ActivateCustomerCommand.builder()
                .customerId(activationLinkSentEvent.getCustomerId())
                .activationKey(activationLinkSentEvent.getActivationKey()).build();

        testFixture.given(userCreatedEvent, activationLinkSentEvent)
                .when(command)
                .expectEventsMatching(sequenceOf(matches(p -> {
                    UserActivatedEvent event = (UserActivatedEvent) p.getPayload();
                    return event.getUserId().equals(command.getCustomerId());
                }), andNoMore()));
    }

    private ActivationLinkSentEvent getActivationLinkSentEvent(UUID customerId) {
        return ActivationLinkSentEvent.builder()
                .customerId(customerId)
                .activationKey(UUID.randomUUID())
                .dateTimeRange(DateTimeRange.create()).build();
    }
}
