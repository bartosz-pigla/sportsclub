package query.model.user;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import query.model.BaseEntity;
import query.model.embeddable.Email;
import query.model.embeddable.PhoneNumber;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserEntity extends BaseEntity {

    private String username;
    private String password;
    private UserType userType;
    @Embedded
    private Email email;
    @Embedded
    private PhoneNumber phoneNumber;
    private boolean activated;
    @OneToOne(fetch = FetchType.LAZY)
    private ActivationLinkEntry activationLink;
    private boolean deleted;
}
