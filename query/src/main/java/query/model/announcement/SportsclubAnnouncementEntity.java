package query.model.announcement;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import query.model.sportsclub.SportsclubEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SportsclubAnnouncementEntity extends BaseAnnouncementEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private SportsclubEntity sportsclub;
}
