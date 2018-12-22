package query.model.announcement;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import query.model.baseEntity.BaseEntity;
import query.model.sportsclub.SportsclubEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AnnouncementEntity extends BaseEntity {

    private String title;
    @Column(length = LONG_STRING_MAX_LENGTH)
    private String content;
    private LocalDateTime lastModificationDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private SportsclubEntity sportsclub;
}
