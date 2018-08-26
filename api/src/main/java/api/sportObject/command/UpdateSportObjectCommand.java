package api.sportObject.command;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.axonframework.commandhandling.TargetAggregateIdentifier;
import query.model.embeddable.Address;
import query.model.embeddable.ImageUrl;

@Data
@AllArgsConstructor
@Builder
public final class UpdateSportObjectCommand {

    @TargetAggregateIdentifier
    private UUID sportObjectId;
    private UUID sportsclubId;
    private String name;
    private String description;
    private Address address;
    private ImageUrl imageUrl;
}
