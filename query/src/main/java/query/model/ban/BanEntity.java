package query.model.ban;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import query.model.baseEntity.BaseEntity;
import query.model.embeddable.InetAddress;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ban_type")
@Getter
@Setter
@NoArgsConstructor
public abstract class BanEntity extends BaseEntity {

    @Embedded
    private InetAddress inetAddress;
}
