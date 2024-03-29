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
import query.model.announcement.AnnouncementEntity;
import query.model.baseEntity.BaseEntity;
import query.model.embeddable.Address;
import query.model.sportobject.SportObjectEntity;

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
    private List<AnnouncementEntity> announcements;
    @OneToOne(mappedBy = "sportsclub")
    private StatuteEntity statute;
    @OneToMany(mappedBy = "headquarter")
    private Set<SportObjectEntity> sportObjects;
}
