package sportsclub.domain.user.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode(of = {"login"})
@Builder
public class UserEntry {
    @Id
    private String login;

    @Setter
    private String password;

    @Setter
    private boolean deleted;

    @Setter
    private boolean activated;
}
