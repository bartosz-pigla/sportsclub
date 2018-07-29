package api.sportsclub.event;

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
public final class StatuteUpdatedEvent extends DomainEvent implements Serializable {

    private static final long serialVersionUID = -2623208907521015496L;

    private UUID statuteId;
    private String title;
    private String description;
}
