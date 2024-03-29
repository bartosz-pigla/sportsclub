package domain.user;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.matches;
import static org.axonframework.test.matchers.Matchers.sequenceOf;
import static org.mockito.Mockito.when;
import static query.model.user.repository.UserQueryExpressions.usernameMatches;

import api.user.event.UserCreatedEvent;
import domain.common.exception.AlreadyCreatedException;
import org.junit.Test;

public final class CreateUserTest extends AbstractUserTest {

    @Test
    public void shouldNotCreateUserWhenAlreadyExists() {
        when(userRepository.exists(usernameMatches(createUserCommand.getUsername()))).thenReturn(true);

        testFixture.given(userCreatedEvent)
                .when(createUserCommand)
                .expectNoEvents()
                .expectException(AlreadyCreatedException.class);
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
