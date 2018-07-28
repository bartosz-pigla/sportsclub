package domain.user;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.matches;
import static org.axonframework.test.matchers.Matchers.sequenceOf;

import api.user.event.UserDeletedEvent;
import domain.common.exception.AlreadyDeletedException;
import org.axonframework.commandhandling.model.AggregateNotFoundException;
import org.junit.Test;

public final class DeleteUserTest extends UserTest {

    @Test
    public void shouldNotDeleteUserWhenNotExists() {
        testFixture.givenNoPriorActivity()
                .when(deleteUserCommand)
                .expectNoEvents()
                .expectException(AggregateNotFoundException.class);
    }

    @Test
    public void shouldNotDeleteUserWhenAlreadyDeleted() {
        testFixture.given(userCreatedEvent, userDeletedEvent)
                .when(deleteUserCommand)
                .expectNoEvents()
                .expectException(AlreadyDeletedException.class);
    }

    @Test
    public void shouldDeleteUserWhenExists() {
        testFixture.given(userCreatedEvent)
                .when(deleteUserCommand)
                .expectEventsMatching(sequenceOf(matches(p -> {
                    UserDeletedEvent event = (UserDeletedEvent) p.getPayload();
                    return event.getUserId().equals(deleteUserCommand.getUserId());
                }), andNoMore()));
    }
}
