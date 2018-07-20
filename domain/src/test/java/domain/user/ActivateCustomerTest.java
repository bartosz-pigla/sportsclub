package domain.user;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.matches;
import static org.axonframework.test.matchers.Matchers.sequenceOf;

import java.time.LocalDateTime;
import java.util.UUID;

import api.user.command.ActivateCustomerCommand;
import api.user.event.ActivationLinkSentEvent;
import api.user.event.CustomerActivatedEvent;
import domain.user.exception.ActivationKeyInvalidException;
import domain.user.exception.ActivationLinkExpiredException;
import domain.user.exception.AlreadyActivatedException;
import org.junit.Test;
import query.model.embeddable.DateTimeRange;

public final class ActivateCustomerTest extends UserTest {

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
                .expectException(ActivationKeyInvalidException.class);
    }

    @Test
    public void shouldNotActivateCustomerWhenCustomerIsAlreadyActivated() {
        ActivationLinkSentEvent activationLinkSentEvent = getActivationLinkSentEvent(userCreatedEvent.getUserId());
        ActivateCustomerCommand command = ActivateCustomerCommand.builder()
                .customerId(activationLinkSentEvent.getCustomerId())
                .activationKey(activationLinkSentEvent.getActivationKey()).build();
        CustomerActivatedEvent customerActivatedEvent = CustomerActivatedEvent.builder()
                .customerId(command.getCustomerId()).build();

        testFixture.given(userCreatedEvent, activationLinkSentEvent, customerActivatedEvent)
                .when(command)
                .expectNoEvents()
                .expectException(AlreadyActivatedException.class);
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
                    CustomerActivatedEvent event = (CustomerActivatedEvent) p.getPayload();
                    return event.getCustomerId().equals(command.getCustomerId());
                }), andNoMore()));
    }

    private ActivationLinkSentEvent getActivationLinkSentEvent(UUID customerId) {
        return ActivationLinkSentEvent.builder()
                .customerId(customerId)
                .activationKey(UUID.randomUUID())
                .dateTimeRange(DateTimeRange.create()).build();
    }
}
