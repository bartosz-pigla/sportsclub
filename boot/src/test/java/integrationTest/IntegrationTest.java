package integrationTest;

import static junit.framework.TestCase.assertTrue;
import static web.common.RequestMappings.SIGN_IN;

import java.util.HashMap;
import java.util.List;

import boot.SportsClubApplication;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import web.signIn.dto.SignInWebCommand;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SportsClubApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
public abstract class IntegrationTest {

    @Autowired
    protected TestRestTemplate restTemplate;

    protected void assertField(String field, String value, List dto) {
        assertTrue(((List<HashMap<String, String>>) dto).stream().anyMatch(
                fieldValueMap -> fieldValueMap.get("field").equals(field) && fieldValueMap.get("code").equals(value)));
    }

    protected String getJwtToken(String username, String password) {
        SignInWebCommand command = new SignInWebCommand(username, password);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(SIGN_IN, command, String.class);
        return responseEntity.getBody();
    }
}
