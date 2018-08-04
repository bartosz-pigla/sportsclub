package domain.sportObject;

import java.util.UUID;

import api.sportObject.event.OpeningTimeDeletedEvent;
import api.sportObject.event.OpeningTimeUpdatedEvent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.axonframework.eventsourcing.EventSourcingHandler;
import query.model.embeddable.OpeningTimeRange;
import query.model.embeddable.Price;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class OpeningTime {

    private UUID openingTimeId;
    private OpeningTimeRange openingTimeRange;
    private Price price;
    private boolean deleted;

    @EventSourcingHandler
    public void on(OpeningTimeUpdatedEvent event) {
        if (event.getOpeningTimeId().equals(openingTimeId)) {
            openingTimeRange = event.getDateRange();
            price = event.getPrice();
        }
    }

    @EventSourcingHandler
    public void on(OpeningTimeDeletedEvent event) {
        if (event.getOpeningTimeId().equals(openingTimeId)) {
            deleted = true;
        }
    }
}
