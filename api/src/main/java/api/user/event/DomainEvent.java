package api.user.event;

import java.util.UUID;

import lombok.Getter;

@Getter
abstract class DomainEvent {

    protected UUID eventId = UUID.randomUUID();
}
