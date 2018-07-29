package integrationTest;

import static junit.framework.TestCase.assertTrue;
import static web.common.RequestMappings.SIGN_IN;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import boot.SportsClubApplication;
import boot.populator.DirectorPopulator;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import web.publicApi.signIn.dto.JwtAuthenticationResponse;
import web.publicApi.signIn.dto.SignInWebCommand;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SportsClubApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
public abstract class IntegrationTest {

    @Autowired
    protected TestRestTemplate restTemplate;
    @Autowired
    private DirectorPopulator directorPopulator;
    @Autowired
    protected CommandGateway commandGateway;

    @Before
    public void setUp() {
        directorPopulator.initializeDirector();
    }

    protected void assertField(String field, String value, List dto) {
        assertTrue(((List<HashMap<String, String>>) dto).stream().anyMatch(
                fieldValueMap -> fieldValueMap.get("field").equals(field) && fieldValueMap.get("code").equals(value)));
    }

    public void signIn(String username, String password) {
        SignInWebCommand command = new SignInWebCommand(username, password);
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
