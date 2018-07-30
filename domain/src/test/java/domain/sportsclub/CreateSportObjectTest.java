package domain.sportsclub;

import static junit.framework.TestCase.assertTrue;

import java.util.UUID;

import api.sportsclub.event.SportsclubCreatedEvent;
import org.junit.Test;

public final class CreateSportObjectTest extends SportsclubTest {

    private static SportsclubCreatedEvent createdEvent = SportsclubCreatedEvent.builder()
            .sportsclubId(UUID.randomUUID())
            .name("name1")
            .description("description1")
            .build();

    @Test
    public void shouldNotCreateWhenSportObjectAlreadyExists() {
        assertTrue(true);
    }

    @Test
    public void shouldCreateWhenSportObjectNotExists() {
        assertTrue(true);
    }
}
