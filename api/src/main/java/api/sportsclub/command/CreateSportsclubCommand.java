package api.sportsclub.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import query.model.embeddable.Address;

@Data
@AllArgsConstructor
@Builder
public final class CreateSportsclubCommand {

    private String name;
    private String description;
    private Address address;
}
