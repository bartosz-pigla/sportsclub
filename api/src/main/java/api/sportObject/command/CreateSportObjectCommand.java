package api.sportObject.command;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import query.model.embeddable.Address;
import query.model.embeddable.ImageUrl;

@Data
@AllArgsConstructor
@Builder
public final class CreateSportObjectCommand {

    private UUID sportsclubId;
    private String name;
    private String description;
    private Address address;
    private ImageUrl imageUrl;
}
