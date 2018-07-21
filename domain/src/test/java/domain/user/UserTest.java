package domain.user;

import static org.springframework.beans.BeanUtils.copyProperties;

import java.util.UUID;

import api.user.command.CreateUserCommand;
import api.user.event.UserCreatedEvent;
import domain.user.activateCustomer.validator.ActivateCustomerValidator;
import domain.user.createUser.validator.CreateUserValidator;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import query.model.embeddable.Email;
import query.model.embeddable.PhoneNumber;
import query.model.user.UserEntity;
import query.model.user.UserType;
import query.repository.UserEntityRepository;

@RunWith(MockitoJUnitRunner.class)
abstract class UserTest {

    protected AggregateTestFixture<User> testFixture;
    protected CreateUserCommand createUserCommand;
    protected UserCreatedEvent userCreatedEvent;
    @Mock
    protected UserEntityRepository userRepository;
    protected UserEntity user;

    @Before
    public void setUp() {
        createAggregateTestFixture();
        createCommand();
        createEvent();
        createEntity();
    }

    private void createAggregateTestFixture() {
        testFixture = new AggregateTestFixture<>(User.class);
        testFixture.setReportIllegalStateChange(false);
        testFixture.registerInjectableResource(new CreateUserValidator(userRepository));
        testFixture.registerInjectableResource(new ActivateCustomerValidator());
        testFixture.registerInjectableResource(userRepository);
    }

    private void createCommand() {
        createUserCommand = CreateUserCommand.builder()
                .username("username")
                .password("password")
                .userType(UserType.CUSTOMER)
                .email(new Email("bartosz.pigla@o2.pl"))
                .phoneNumber(new PhoneNumber("+48664220607")).build();
    }

    private void createEvent() {
        userCreatedEvent = new UserCreatedEvent();
        copyProperties(createUserCommand, userCreatedEvent);
        userCreatedEvent.setUserId(UUID.randomUUID());
    }

    private void createEntity() {
        user = new UserEntity();
        copyProperties(userCreatedEvent, user);
        user.setId(userCreatedEvent.getUserId());
    }
}
