package integrationTest;

import static junit.framework.TestCase.assertTrue;

import java.util.HashMap;
import java.util.List;

import boot.SportsClubApplication;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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
}
