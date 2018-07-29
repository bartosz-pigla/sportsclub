package query.model.statute;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import query.model.BaseEntity;
import query.model.sportsclub.SportsclubEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class StatuteEntity extends BaseEntity {

    private String title;
    private String description;
    @OneToOne(fetch = FetchType.LAZY)
    private SportsclubEntity sportsclub;
}
