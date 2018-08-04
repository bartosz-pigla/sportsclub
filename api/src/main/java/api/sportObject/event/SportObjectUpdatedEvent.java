package api.sportObject.event;

import java.io.Serializable;
import java.net.URL;
import java.util.UUID;

import api.common.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import query.model.embeddable.Address;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class SportObjectUpdatedEvent extends DomainEvent implements Serializable {

    private static final long serialVersionUID = 3718741452212133614L;

    private UUID sportObjectId;
    private UUID sportsclubId;
    private String name;
    private String description;
    private Address address;
    private URL image;
}
