package integrationTest;

import static junit.framework.TestCase.assertTrue;
import static web.common.RequestMappings.SIGN_IN;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import boot.SportsClubApplication;
import boot.populator.UserPopulator;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import web.publicApi.signIn.dto.JwtAuthenticationResponse;
import web.publicApi.signIn.dto.SignInWebCommand;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SportsClubApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
public abstract class IntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;
    protected RestTemplate restTemplate;
    @Autowired
    private UserPopulator userPopulator;
    @Autowired
    protected CommandGateway commandGateway;

    @Before
    public void setUp() {
        userPopulator.initializeDirector();
        userPopulator.initializeCustomer();
        setUpPathRestTemplate();
    }

    private void setUpPathRestTemplate() {
        this.restTemplate = testRestTemplate.getRestTemplate();
        HttpClient httpClient = HttpClientBuilder.create().build();
        this.restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
    }

    protected void assertField(String field, String value, List dto) {
        assertTrue(((List<HashMap<String, String>>) dto).stream().anyMatch(
                fieldValueMap -> fieldValueMap.get("field").equals(field) && fieldValueMap.get("code").equals(value)));
    }

    public void signIn(String username, String password) {
        SignInWebCommand command = new SignInWebCommand(username, password);
        ResponseEntity<JwtAuthenticationResponse> responseEntity = restTemplate.postForEntity(SIGN_IN, command, JwtAuthenticationResponse.class);
        JwtAuthenticationResponse jwtResponse = responseEntity.getBody();

        restTemplate.setInterceptors(
                Collections.singletonList((request, body, execution) -> {
                    request.getHeaders()
                            .add("Authorization", jwtResponse.toString());
                    return execution.execute(request, body);
                }));
    }

    public <T, U> ResponseEntity<U> put(String path, T requestBody, Class<U> responseType, Object... pathParameters) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<T> entity = new HttpEntity<>(requestBody, headers);

        return restTemplate.exchange(
                path,
                HttpMethod.PUT,
                entity,
                responseType,
                pathParameters);
    }

    public <T, U> ResponseEntity<U> patch(String path, T requestBody, Class<U> responseType, Object... pathParameters) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<T> entity = new HttpEntity<>(requestBody, headers);
        return restTemplate.exchange(
                path,
                HttpMethod.PATCH,
                entity,
                responseType,
                pathParameters);
    }

    public <T> ResponseEntity<T> delete(String path, Class<T> responseType, Object... pathParameters) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<T> entity = new HttpEntity<>(null, headers);

        return restTemplate.exchange(
                path,
                HttpMethod.DELETE,
                entity,
                responseType,
                pathParameters);
    }
}
