package domain.user;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.matches;
import static org.axonframework.test.matchers.Matchers.sequenceOf;

import api.user.event.UserActivatedEvent;
import domain.common.exception.AlreadyDeletedException;
import domain.user.activation.common.exception.AlreadyActivatedException;
import org.junit.Test;

public final class ActivateUserTest extends AbstractUserTest {

    @Test
    public void shouldNotActivateWhenIsAlreadyActivated() {
        UserActivatedEvent userActivatedEvent = UserActivatedEvent.builder()
                .userId(activateUserCommand.getUserId()).build();

        testFixture.given(userCreatedEvent, userActivatedEvent)
                .when(activateUserCommand)
                .expectNoEvents()
                .expectException(AlreadyActivatedException.class);
    }

    @Test
    public void shouldNotActivateWhenIsAlreadyDeleted() {
        testFixture.given(userCreatedEvent, userDeletedEvent)
                .when(activateUserCommand)
                .expectNoEvents()
                .expectException(AlreadyDeletedException.class);
    }

    @Test
    public void shouldActivateWhenExistsAndIsNotDeleted() {
        testFixture.given(userCreatedEvent)
                .when(activateUserCommand)
                .expectEventsMatching(sequenceOf(matches(p -> {
                    UserActivatedEvent event = (UserActivatedEvent) p.getPayload();
                    return event.getUserId().equals(activateUserCommand.getUserId());
                }), andNoMore()));
    }
}
