package domain.sportObject;

import java.util.UUID;

import api.sportObject.sportObjectPosition.event.SportObjectPositionDeletedEvent;
import api.sportObject.sportObjectPosition.event.SportObjectPositionUpdatedEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.axonframework.eventsourcing.EventSourcingHandler;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SportObjectPosition {

    private UUID sportObjectPositionId;
    private String name;
    private boolean deleted;

    SportObjectPosition(UUID sportObjectPositionId, String name) {
        this.sportObjectPositionId = sportObjectPositionId;
        this.name = name;
    }

    @EventSourcingHandler
    public void on(SportObjectPositionDeletedEvent event) {
        if (sportObjectPositionId.equals(event.getSportObjectPositionId())) {
            deleted = true;
        }
    }

    @EventSourcingHandler
    public void on(SportObjectPositionUpdatedEvent event) {
        if (sportObjectPositionId.equals(event.getSportObjectPositionId())) {
            name = event.getName();
        }
    }
}
