package api.sportObject.command;

import java.net.URL;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import query.model.embeddable.Address;

@Data
@AllArgsConstructor
@Builder
public final class CreateSportObjectCommand {

    private UUID sportsclubId;
    private String name;
    private String description;
    private Address address;
    private URL image;
}
