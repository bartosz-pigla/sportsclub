package api.booking.event;

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
public final class BookingCanceledEvent extends DomainEvent implements Serializable {

    private static final long serialVersionUID = -3785962110916932928L;

    private UUID bookingId;
}
