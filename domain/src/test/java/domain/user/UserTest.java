package domain.user;

import static org.springframework.beans.BeanUtils.copyProperties;

import java.util.UUID;

import api.user.command.ActivateUserCommand;
import api.user.command.CreateUserCommand;
import api.user.command.DeactivateUserCommand;
import api.user.command.DeleteUserCommand;
import api.user.event.UserCreatedEvent;
import api.user.event.UserDeletedEvent;
import domain.user.activation.customer.service.ActivateCustomerValidator;
import domain.user.activation.customer.service.SendActivationLinkValidator;
import domain.user.activation.user.service.ActivateUserValidator;
import domain.user.activation.user.service.DeactivateUserValidator;
import domain.user.createUser.service.CreateUserValidator;
import domain.user.deleteUser.service.DeleteUserValidator;
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
    protected ActivateUserCommand activateUserCommand;
    protected DeactivateUserCommand deactivateUserCommand;
    protected UserCreatedEvent userCreatedEvent;
    protected DeleteUserCommand deleteUserCommand;
    protected UserDeletedEvent userDeletedEvent;
    @Mock
    protected UserEntityRepository userRepository;
    protected UserEntity user;

    @Before
    public void setUp() {
        setUpAggregateTestFixture();
        setUpCreateUserCommand();
        setUpUserCreatedEvent();
        createEntity();
        setUpDeleteUserCommand();
        setUpUserDeletedEvent();
        setUpActivateUserCommand();
        setUpDeactivateUserCommand();
    }

    private void setUpAggregateTestFixture() {
        testFixture = new AggregateTestFixture<>(User.class);
        testFixture.setReportIllegalStateChange(false);
        testFixture.registerInjectableResource(new CreateUserValidator(userRepository));
        testFixture.registerInjectableResource(new ActivateCustomerValidator());
        testFixture.registerInjectableResource(new SendActivationLinkValidator());
        testFixture.registerInjectableResource(new DeleteUserValidator());
        testFixture.registerInjectableResource(new ActivateUserValidator());
        testFixture.registerInjectableResource(new DeactivateUserValidator());
        testFixture.registerInjectableResource(userRepository);
    }

    private void setUpCreateUserCommand() {
        createUserCommand = CreateUserCommand.builder()
                .username("username")
                .password("password")
                .userType(UserType.CUSTOMER)
                .email(new Email("bartosz.pigla@o2.pl"))
                .phoneNumber(new PhoneNumber("+48664220607")).build();
    }

    private void setUpUserCreatedEvent() {
        userCreatedEvent = new UserCreatedEvent();
        copyProperties(createUserCommand, userCreatedEvent);
        userCreatedEvent.setUserId(UUID.randomUUID());
    }

    private void createEntity() {
        user = new UserEntity();
        copyProperties(userCreatedEvent, user);
        user.setId(userCreatedEvent.getUserId());
    }

    private void setUpDeleteUserCommand() {
        deleteUserCommand = DeleteUserCommand.builder()
                .userId(userCreatedEvent.getUserId()).build();
    }

    private void setUpUserDeletedEvent() {
        userDeletedEvent = UserDeletedEvent.builder()
                .userId(deleteUserCommand.getUserId()).build();
    }

    private void setUpActivateUserCommand() {
        activateUserCommand = ActivateUserCommand.builder()
                .userId(userCreatedEvent.getUserId()).build();
    }

    private void setUpDeactivateUserCommand() {
        deactivateUserCommand = DeactivateUserCommand.builder()
                .userId(userCreatedEvent.getUserId()).build();
    }
}
