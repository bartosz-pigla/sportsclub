package query.model.user;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import query.model.BaseEntity;
import query.model.embeddable.DateTimeRange;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ActivationLinkEntry extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 7388807433862162994L;

    @Embedded
    private DateTimeRange activationDeadlineRange;
    @OneToOne(fetch = FetchType.LAZY)
    private UserEntity customer;

    public ActivationLinkEntry(UUID activationId, DateTimeRange activationDeadlineRange, UserEntity customer) {
        this.id = activationId;
        this.activationDeadlineRange = activationDeadlineRange;
        this.customer = customer;
    }
}
