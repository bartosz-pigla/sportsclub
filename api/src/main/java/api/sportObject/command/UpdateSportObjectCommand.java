package api.sportObject.command;

import java.net.URL;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.axonframework.commandhandling.TargetAggregateIdentifier;
import query.model.embeddable.Address;

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
    private URL image;
}
