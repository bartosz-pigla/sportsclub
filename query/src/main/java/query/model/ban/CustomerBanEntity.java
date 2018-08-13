package query.model.ban;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import query.model.baseEntity.BaseEntity;
import query.model.user.UserEntity;

@Entity
@DiscriminatorValue("customer")
@Getter
@Setter
@NoArgsConstructor
public class CustomerBanEntity extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    private UserEntity customer;
}
