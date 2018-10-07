package query.model.sportobject;

import java.time.LocalTime;
import java.util.UUID;

import lombok.Value;
import query.model.embeddable.PositionsCount;
import query.model.embeddable.Price;

@Value
public final class PositionsWithOpeningTimes {

    UUID positionId;
    String name;
    PositionsCount positionsCount;
    UUID openingTimeId;
    LocalTime startTime;
    LocalTime finishTime;
    Price price;

}
