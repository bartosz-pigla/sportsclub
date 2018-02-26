package sportsclub.domain.user;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import sportsclub.api.user.command.ActivateUserCommand;
import sportsclub.api.user.command.CreateUserCommand;
import sportsclub.api.user.event.UserActivatedEvent;
import sportsclub.api.user.event.UserCreatedEvent;
import sportsclub.domain.user.model.User;
import sportsclub.domain.user.model.UserEntry;
import sportsclub.domain.user.service.UserEntryRepository;
import sportsclub.domain.user.service.UserValidator;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserTest {
    private AggregateTestFixture<User> testFixture;

    @Mock
    private UserEntryRepository userEntryRepository;

    private Errors errors;

    private UserEntry userEntry = UserEntry.builder()
            .login("login")
            .password("password")
            .build();

    @Before
    public void setUp() throws Exception {
        testFixture = new AggregateTestFixture<>(User.class);

        testFixture.registerInjectableResource(new UserValidator(userEntryRepository));

        errors = new BeanPropertyBindingResult(userEntry, "userEntry");
        testFixture.registerInjectableResource(errors);
    }

    @Test
    public void shouldAddEventWhenCreateNewUser() throws Exception {
        when(userEntryRepository.findOne("login")).thenReturn(
                null);

        testFixture.givenNoPriorActivity()
                .when(new CreateUserCommand("login", "password"))
                .expectEvents(new UserCreatedEvent("login", "password"));

        assertFalse(errors.hasErrors());
    }

    @Test
    public void shouldNotAddEventWhenCreateUserWithTheSameLogin() throws Exception {
        when(userEntryRepository.findOne("login")).thenReturn(
                userEntry);

        UserCreatedEvent event = new UserCreatedEvent("login", "password");
        testFixture.given(event)
                .when(new CreateUserCommand("login", "password"))
                .expectNoEvents();

        assertTrue(errors.hasErrors());
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

//        testFixture
//                .given(new UserCreatedEvent("login", "password"))
//                .when(new ActivateUserCommand("login"))
//                .expectEvents(new UserActivatedEvent("login"));
    }
}
