package integrationTest.publicApi.customer;

import static web.common.RequestMappings.PUBLIC_API_SIGN_UP;

import api.user.command.CreateUserCommand;
import integrationTest.AbstractUserItTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import query.model.embeddable.Email;
import query.model.embeddable.PhoneNumber;
import query.model.user.UserType;

public final class SignUpCustomerItTest extends AbstractUserItTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DirtiesContext
    public void shouldSendErrorMessageWhenCreateCustomerIsEmpty() {
        shouldSendErrorMessageWhenCreateUserIsEmpty(PUBLIC_API_SIGN_UP);
    }

    @Test
    @DirtiesContext
    public void shouldNotSaveCustomerWhenUserWithGivenUsernameAlreadyExists() {
        commandGateway.send(CreateUserCommand.builder()
                .email(new Email(createUserWebCommand.getEmail()))
                .password(passwordEncoder.encode(createUserWebCommand.getPassword()))
                .phoneNumber(new PhoneNumber(createUserWebCommand.getPhoneNumber()))
                .username(createUserWebCommand.getUsername())
                .userType(UserType.CUSTOMER)
                .build());

        shouldNotSaveUserWhenUserWithGivenUsernameAlreadyExists(PUBLIC_API_SIGN_UP);
    }

    @Test
    @DirtiesContext
    public void shouldSaveCustomerWhenAllFieldsAreValid() {
        shouldSaveUserWhenAllFieldsAreValid(PUBLIC_API_SIGN_UP);
    }
}
