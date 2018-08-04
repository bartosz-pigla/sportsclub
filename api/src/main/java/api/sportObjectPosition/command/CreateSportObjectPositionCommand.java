package api.sportObjectPosition.command;

import java.util.UUID;

import javax.persistence.Embedded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import query.model.embeddable.PositiveNumber;

@Data
@AllArgsConstructor
@Builder
public final class CreateSportObjectPositionCommand {

    @AggregateIdentifier
    private UUID sportObjectId;
    private String name;
    private String description;
    @Embedded
    private PositiveNumber positionsCount;
}
