package query.model.sportsclub;

import java.util.List;
import java.util.Set;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import query.model.BaseEntity;
import query.model.announcement.SportsclubAnnouncementEntity;
import query.model.embeddable.Address;
import query.model.sportobject.SportObjectEntity;
import query.model.statute.StatuteEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SportsclubEntity extends BaseEntity {

    private String name;
    private String description;
    @Embedded
    private Address address;
    @OneToMany(mappedBy = "sportsclub")
    private List<SportsclubAnnouncementEntity> announcements;
    @OneToOne(mappedBy = "sportsclub")
    private StatuteEntity statute;
    @OneToMany(mappedBy = "headquarter")
    private Set<SportObjectEntity> sportObjects;
}
