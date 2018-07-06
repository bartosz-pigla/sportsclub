package query.model.user;

import java.util.UUID;

import javax.persistence.Embedded;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import query.model.BaseEntity;
import query.model.embeddable.DateTimeRange;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ActivationLinkEntry extends BaseEntity {

    private UUID key;
    @Embedded
    private DateTimeRange activationDeadlineRange;
}
