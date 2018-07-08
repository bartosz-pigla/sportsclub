package integrationTest;

import static junit.framework.TestCase.assertTrue;

import java.util.HashMap;
import java.util.List;

public class IntegrationTest {

    protected void assertField(String field, String value, List dto) {
        assertTrue(((List<HashMap<String, String>>) dto).stream().anyMatch(
                fieldValueMap -> fieldValueMap.get("field").equals(field) && fieldValueMap.get("code").equals(value)));
    }
}
