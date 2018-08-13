package query.model.sportobject;

import java.net.URL;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import query.model.baseEntity.BaseEntity;
import query.model.embeddable.Address;
import query.model.sportsclub.SportsclubEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SportObjectEntity extends BaseEntity {

    private String name;
    private String description;
    @Embedded
    private Address address;
    private URL image;
    @ManyToOne(fetch = FetchType.LAZY)
    private SportsclubEntity headquarter;
    @OneToMany
    private List<OpeningTimeEntity> openingHours;
}
