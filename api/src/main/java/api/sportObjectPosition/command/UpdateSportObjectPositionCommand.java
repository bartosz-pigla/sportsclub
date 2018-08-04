package api.sportObjectPosition.command;

import java.util.UUID;

import javax.persistence.Embedded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.axonframework.commandhandling.TargetAggregateIdentifier;
import query.model.embeddable.PositiveNumber;

@Data
@AllArgsConstructor
@Builder
public final class UpdateSportObjectPositionCommand {

    @TargetAggregateIdentifier
    private UUID sportObjectId;
    private UUID sportObjectPositionId;
    private String name;
    private String description;
    @Embedded
    private PositiveNumber positionsCount;

}
