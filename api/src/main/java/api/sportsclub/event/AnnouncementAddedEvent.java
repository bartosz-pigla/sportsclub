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
public final class AnnouncementAddedEvent extends DomainEvent implements Serializable {

    private static final long serialVersionUID = 6694894552644687956L;

    private UUID announcementId;
    private UUID sportsclubId;
    private String title;
    private String content;
}
