package query.model.ban;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("inet")
@Getter
@Setter
@NoArgsConstructor
public class InetBanInet extends BanEntity {

}
