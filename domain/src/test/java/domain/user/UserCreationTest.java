package domain.user;

import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.matches;
import static org.axonframework.test.matchers.Matchers.sequenceOf;
import static org.mockito.Mockito.doThrow;

import java.util.UUID;

import api.user.command.CreateUserCommand;
import api.user.event.UserCreatedEvent;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import query.model.embeddable.Email;
import query.model.embeddable.PhoneNumber;
import query.model.user.UserType;

@RunWith(MockitoJUnitRunner.class)
public class UserCreationTest {

    private AggregateTestFixture<User> testFixture;
    private CreateUserCommand command;
    @Mock
    private UserValidator userValidator;

    @Before
    public void setUp() throws Exception {
        testFixture = new AggregateTestFixture<>(User.class);
        testFixture.registerInjectableResource(userValidator);
        testFixture.setReportIllegalStateChange(false);
        command = CreateUserCommand.builder()
                .username("username")
                .password("password")
                .userType(UserType.CUSTOMER)
                .email(new Email("bartosz.pigla@o2.pl"))
                .phoneNumber(new PhoneNumber("+48664220607")).build();
    }

    @Test
    public void shouldNotCreateUserWhenAlreadyExists() {
        UserCreatedEvent event = UserCreatedEvent.builder()
                .id(UUID.randomUUID())
                .username(command.getUsername()).build();

        doThrow(new UserCreationException()).when(userValidator).validate(command);

        testFixture.given(event)
                .when(command)
                .expectNoEvents()
                .expectException(UserCreationException.class);
    }

    @Test
    public void shouldCreateUserWhenHasUniqueUsername() {
        testFixture.givenNoPriorActivity()
                .when(command)
                .expectEventsMatching(sequenceOf(matches(p -> {
                    UserCreatedEvent event = (UserCreatedEvent) p.getPayload();
                    return event.getUsername().equals("username");
                }), andNoMore()));
    }
}
