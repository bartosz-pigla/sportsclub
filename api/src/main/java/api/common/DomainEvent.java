package api.common;

import java.util.UUID;

import lombok.Getter;

@Getter
public abstract class DomainEvent {

    protected UUID eventId = UUID.randomUUID();
}
