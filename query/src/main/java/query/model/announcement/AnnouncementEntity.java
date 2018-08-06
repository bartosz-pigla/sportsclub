package query.model.announcement;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import query.model.BaseEntity;
import query.model.sportsclub.SportsclubEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AnnouncementEntity extends BaseEntity {

    protected String title;
    protected String content;
    protected LocalDateTime lastModificationDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private SportsclubEntity sportsclub;
}
