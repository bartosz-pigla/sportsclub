package api.user.event;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import query.model.embeddable.DateTimeRange;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivationLinkSentEvent extends DomainEvent implements Serializable {

    private static final long serialVersionUID = -8820120540436741716L;

    private UUID customerId;
    private UUID activationKey;
    private DateTimeRange dateTimeRange;
}
