package domain.user;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.matches;
import static org.axonframework.test.matchers.Matchers.sequenceOf;
import static org.mockito.Mockito.doThrow;

import api.user.event.UserCreatedEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public final class CreateUserTest extends UserTest {

    @Test
    public void shouldNotCreateUserWhenAlreadyExists() {
        doThrow(new UserCreationException()).when(userValidator).validate(createUserCommand);

        testFixture.given(userCreatedEvent)
                .when(createUserCommand)
                .expectNoEvents()
                .expectException(UserCreationException.class);
    }

    @Test
    public void shouldCreateUserWhenHasUniqueUsername() {
        testFixture.givenNoPriorActivity()
                .when(createUserCommand)
                .expectEventsMatching(sequenceOf(matches(p -> {
                    UserCreatedEvent event = (UserCreatedEvent) p.getPayload();
                    return event.getUsername().equals("username");
                }), andNoMore()));
    }
}
