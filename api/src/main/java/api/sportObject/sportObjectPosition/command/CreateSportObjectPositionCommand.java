package api.sportObject.sportObjectPosition.command;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.axonframework.commandhandling.TargetAggregateIdentifier;
import query.model.embeddable.PositionsCount;

@Data
@AllArgsConstructor
@Builder
public final class CreateSportObjectPositionCommand {

    @TargetAggregateIdentifier
    private UUID sportObjectId;
    private String name;
    private String description;
    private PositionsCount positionsCount;
}
