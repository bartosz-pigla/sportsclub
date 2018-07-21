package integrationTest.user;

import integrationTest.IntegrationTest;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import query.repository.UserEntityRepository;
import web.signUp.dto.CreateCustomerWebCommand;

abstract class AbstractCustomerItTest extends IntegrationTest {

    @Autowired
    protected UserEntityRepository userRepository;
    protected CreateCustomerWebCommand createCustomerCommand;

    @Before
    public void initCustomerCommand() {
        createCustomerCommand = CreateCustomerWebCommand.builder()
                .username("username")
                .password("password")
                .email("bartosz.pigla@o2.pl")
                .phoneNumber("+48664330504").build();
    }

}
