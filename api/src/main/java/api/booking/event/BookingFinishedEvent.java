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
public final class BookingFinishedEvent extends DomainEvent implements Serializable {

    private static final long serialVersionUID = 2096032889026474701L;

    private UUID bookingId;
}
