package api.sportObject.event;

import java.io.Serializable;
import java.util.UUID;

import api.common.DomainEvent;
import query.model.embeddable.Address;

final class SportObjectCreatedEvent extends DomainEvent implements Serializable {

    private static final long serialVersionUID = -5918656324194168747L;

    private UUID sportObjectId;
    private String name;
    private String description;
    private Address address;
}
