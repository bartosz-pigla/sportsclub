package api.sportObject.command;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import query.model.embeddable.Address;

@Data
@AllArgsConstructor
@Builder
public final class CreateSportObjectCommand {

    @AggregateIdentifier
    private UUID sportsclubId;
    private String name;
    private String description;
    private Address address;
}
