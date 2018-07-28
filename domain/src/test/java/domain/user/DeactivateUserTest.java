package domain.user;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.matches;
import static org.axonframework.test.matchers.Matchers.sequenceOf;

import api.user.event.UserActivatedEvent;
import api.user.event.UserDeactivatedEvent;
import domain.common.exception.AlreadyDeletedException;
import domain.user.activation.common.exception.AlreadyDeactivatedException;
import org.junit.Test;

public final class DeactivateUserTest extends UserTest {

    @Test
    public void shouldNotDeactivateWhenIsAlreadyDeactivated() {
        UserDeactivatedEvent userDeactivatedEvent = UserDeactivatedEvent.builder()
                .userId(activateUserCommand.getUserId()).build();

        testFixture.given(userCreatedEvent, userDeactivatedEvent)
                .when(deactivateUserCommand)
                .expectNoEvents()
                .expectException(AlreadyDeactivatedException.class);
    }

    @Test
    public void shouldNotDeactivateWhenIsAlreadyDeleted() {
        UserActivatedEvent userActivatedEvent = UserActivatedEvent.builder()
                .userId(activateUserCommand.getUserId()).build();

        testFixture.given(userCreatedEvent, userActivatedEvent, userDeletedEvent)
                .when(deactivateUserCommand)
                .expectNoEvents()
                .expectException(AlreadyDeletedException.class);
    }

    @Test
    public void shouldDeactivateWhenExistsAndIsNotDeleted() {
        UserActivatedEvent userActivatedEvent = UserActivatedEvent.builder()
                .userId(activateUserCommand.getUserId()).build();

        testFixture.given(userCreatedEvent, userActivatedEvent)
                .when(deactivateUserCommand)
                .expectEventsMatching(sequenceOf(matches(p -> {
                    UserDeactivatedEvent event = (UserDeactivatedEvent) p.getPayload();
                    return event.getUserId().equals(activateUserCommand.getUserId());
                }), andNoMore()));
    }
}
