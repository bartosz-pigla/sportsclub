package sportsclub.domain.user;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.Before;
import org.junit.Test;
import sportsclub.api.user.command.CreateUserCommand;
import sportsclub.api.user.event.UserCreatedEvent;
import sportsclub.domain.user.exception.UserAlreadyExistsException;
import sportsclub.domain.user.model.User;

public class UserTest {
    private AggregateTestFixture<User> testFixture;

    @Before
    public void setUp() throws Exception {
        testFixture = new AggregateTestFixture<>(User.class);
    }

    @Test
    public void shouldAddEventWhenCreateNewUser() throws Exception {
        testFixture.givenNoPriorActivity()
                .when(new CreateUserCommand("login", "password"))
                .expectEvents(new UserCreatedEvent("login", "password"));
    }

    @Test
    public void shouldAddEventWhenCreateUserWithTheSameLogin() throws Exception {
        UserCreatedEvent event = new UserCreatedEvent("login", "password");
        testFixture.given(event)
                .when(new CreateUserCommand("login", "password"))
                .expectNoEvents();
    }
}
