package query.model.announcement;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import query.model.sportobject.SportObjectEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SportObjectAnnouncementEntity extends BaseAnnouncementEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private SportObjectEntity sportObject;
}
