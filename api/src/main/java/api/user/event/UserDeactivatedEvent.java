package api.user.event;

import java.io.Serializable;
import java.util.UUID;

import api.common.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class UserDeactivatedEvent extends DomainEvent implements Serializable {

    private static final long serialVersionUID = -2133605999185363201L;

    private UUID userId;
}
