package domain.user;

import static org.springframework.beans.BeanUtils.copyProperties;

import java.util.UUID;

import api.user.command.CreateUserCommand;
import api.user.event.UserCreatedEvent;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.Before;
import org.mockito.Mock;
import query.model.embeddable.Email;
import query.model.embeddable.PhoneNumber;
import query.model.user.UserType;
import query.repository.UserEntityRepository;

abstract class UserTest {

    protected AggregateTestFixture<User> testFixture;
    protected CreateUserCommand createUserCommand;
    protected UserCreatedEvent userCreatedEvent;
    @Mock
    protected UserValidator userValidator;
    @Mock
    protected UserEntityRepository userRepository;

    @Before
    public void setUp() {
        testFixture = new AggregateTestFixture<>(User.class);
        testFixture.registerInjectableResource(userValidator);
        testFixture.registerInjectableResource(userRepository);
        testFixture.setReportIllegalStateChange(false);
        createUserCommand = CreateUserCommand.builder()
                .username("username")
                .password("password")
                .userType(UserType.CUSTOMER)
                .email(new Email("bartosz.pigla@o2.pl"))
                .phoneNumber(new PhoneNumber("+48664220607")).build();
        userCreatedEvent = new UserCreatedEvent();
        copyProperties(createUserCommand, userCreatedEvent);
        userCreatedEvent.setUserId(UUID.randomUUID());
    }
}
