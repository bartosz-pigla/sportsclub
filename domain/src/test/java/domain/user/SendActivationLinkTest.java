package domain.user;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.matches;
import static org.axonframework.test.matchers.Matchers.sequenceOf;
import static query.model.embeddable.DateTimeRange.DEFAULT_DAYS_DIFFERENCE;

import java.util.UUID;

import api.user.command.SendActivationLinkCommand;
import api.user.event.ActivationLinkSentEvent;
import domain.user.deleteUser.exception.UserAlreadyDeletedException;
import org.axonframework.commandhandling.model.AggregateNotFoundException;
import org.junit.Test;

public final class SendActivationLinkTest extends UserTest {

    @Test
    public void shouldNotSendActivationLinkWhenUserNotExists() {
        SendActivationLinkCommand command = SendActivationLinkCommand.builder()
                .customerId(UUID.randomUUID()).build();

        testFixture.givenNoPriorActivity()
                .when(command)
                .expectNoEvents()
                .expectException(AggregateNotFoundException.class);
    }

    @Test
    public void shouldNotSendActivationLinkWhenUserIsDeleted() {
        SendActivationLinkCommand command = SendActivationLinkCommand.builder()
                .customerId(userCreatedEvent.getUserId()).build();

        testFixture.given(userCreatedEvent, userDeletedEvent)
                .when(command)
                .expectNoEvents()
                .expectException(UserAlreadyDeletedException.class);
    }

    @Test
    public void shouldSendActivationLinkWhenUserExists() {
        SendActivationLinkCommand command = SendActivationLinkCommand.builder()
                .customerId(userCreatedEvent.getUserId()).build();

        testFixture.given(userCreatedEvent)
                .when(command)
                .expectEventsMatching(sequenceOf(matches(p -> {
                    ActivationLinkSentEvent event = (ActivationLinkSentEvent) p.getPayload();
                    return event.getDateTimeRange().getDayDifference() == DEFAULT_DAYS_DIFFERENCE;
                }), andNoMore()));
    }
}
