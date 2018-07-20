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
public final class CustomerActivatedEvent extends DomainEvent implements Serializable {

    private static final long serialVersionUID = -6807130905841516796L;

    private UUID customerId;
}
