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
public final class AnnouncementDeletedEvent extends DomainEvent implements Serializable {

    private static final long serialVersionUID = -5030159438785887863L;

    private UUID announcementId;
}
