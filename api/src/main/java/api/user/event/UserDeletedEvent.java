package api.user.event;

import java.io.Serializable;
import java.util.UUID;

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
public final class UserDeletedEvent extends DomainEvent implements Serializable {

    private static final long serialVersionUID = 7704622900229212426L;

    private UUID userId;
}
