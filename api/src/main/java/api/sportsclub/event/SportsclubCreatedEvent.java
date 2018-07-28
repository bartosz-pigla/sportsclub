package api.sportsclub.event;

import java.io.Serializable;
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
public final class SportsclubCreatedEvent extends DomainEvent implements Serializable {

    private static final long serialVersionUID = 4683057961612709209L;

    private UUID sportsclubId;
    private String name;
    private String description;
    private Address address;
}
