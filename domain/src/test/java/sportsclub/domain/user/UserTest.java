package sportsclub.domain.user;

import org.axonframework.commandhandling.model.AggregateNotFoundException;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import sportsclub.api.user.command.ActivateUserCommand;
import sportsclub.api.user.command.CreateUserCommand;
import sportsclub.api.user.event.UserActivatedEvent;
import sportsclub.api.user.event.UserCreatedEvent;
import sportsclub.api.validation.ValidationException;
import sportsclub.domain.user.model.User;
import sportsclub.query.user.model.UserEntry;
import sportsclub.query.user.service.UserEntryRepository;
import sportsclub.domain.user.service.UserValidator;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserTest {
    private AggregateTestFixture<User> testFixture;

    @Mock
    private UserEntryRepository userEntryRepository;

    @Mock
    private MessageSource messageSource;

    private UserEntry userEntry = UserEntry.builder()
            .login("login")
            .password("password")
            .build();

    @Before
    public void setUp() throws Exception {
        testFixture = new AggregateTestFixture<>(User.class);
        testFixture.registerInjectableResource(new UserValidator(userEntryRepository, messageSource));
    }

    @Test
    public void shouldAddEventWhenCreateNewUser() throws Exception {
        when(userEntryRepository.findOne("login")).thenReturn(
                null);

        testFixture.givenNoPriorActivity()
                .when(new CreateUserCommand("login", "password"))
                .expectEvents(new UserCreatedEvent("login", "password"));
    }

    @Test
    public void shouldNotAddEventWhenCreateUserWithTheSameLogin() throws Exception {
        when(userEntryRepository.findOne("login")).thenReturn(
                userEntry);

        UserCreatedEvent event = new UserCreatedEvent("login", "password");
        testFixture.given(event)
                .when(new CreateUserCommand("login", "password"))
                .expectNoEvents()
                .expectException(ValidationException.class);
    }

    @Test
    public void shouldAddEventWhenActivateUserWithLoginThatExists() throws Exception {
        when(userEntryRepository.findOne("login"))
                .thenReturn(userEntry);
        testFixture.setReportIllegalStateChange(false);

        testFixture
                .given(new UserCreatedEvent("login", "password"))
                .when(new ActivateUserCommand("login"))
                .expectEvents(new UserActivatedEvent("login"));
    }

    @Test
    public void shouldNotEventWhenActivateUserWithLoginThatNotExists() throws Exception {
        when(userEntryRepository.findOne("login"))
                .thenReturn(userEntry);
        testFixture.setReportIllegalStateChange(false);

        testFixture
                .givenNoPriorActivity()
                .when(new ActivateUserCommand("login"))
                .expectException(AggregateNotFoundException.class);
    }
}
