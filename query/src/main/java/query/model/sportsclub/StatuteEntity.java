package query.model.sportsclub;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import query.model.baseEntity.BaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class StatuteEntity extends BaseEntity {

    private String title;
    @Column(length = LONG_STRING_MAX_LENGTH)
    private String description;
    @OneToOne(fetch = FetchType.LAZY)
    private SportsclubEntity sportsclub;
}
