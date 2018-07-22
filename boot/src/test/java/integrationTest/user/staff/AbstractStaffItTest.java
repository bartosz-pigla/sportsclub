package integrationTest.user.staff;

import static web.common.RequestMappings.SIGN_IN;

import java.util.Collections;

import integrationTest.user.AbstractUserItTest;
import org.junit.Before;
import org.springframework.http.ResponseEntity;
import web.signIn.dto.JwtAuthenticationResponse;
import web.signIn.dto.SignInWebCommand;

public abstract class AbstractStaffItTest extends AbstractUserItTest {

    @Before
    public void signInAsDirector() {
        SignInWebCommand command = new SignInWebCommand("superuser", "password");
        ResponseEntity<JwtAuthenticationResponse> responseEntity = restTemplate.postForEntity(SIGN_IN, command, JwtAuthenticationResponse.class);
        JwtAuthenticationResponse jwtResponse = responseEntity.getBody();

        restTemplate.getRestTemplate().setInterceptors(
                Collections.singletonList((request, body, execution) -> {
                    request.getHeaders()
                            .add("Authorization", jwtResponse.toString());
                    return execution.execute(request, body);
                }));
    }
}
