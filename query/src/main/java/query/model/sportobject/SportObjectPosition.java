package query.model.sportobject;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import query.model.BaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SportObjectPosition extends BaseEntity {

    private String name;
    private String description;

}
