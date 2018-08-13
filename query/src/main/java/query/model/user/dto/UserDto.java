package query.model.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class UserDto {

    private String username;
    private String userType;
    private String email;
    private String phoneNumber;
    private boolean activated;
    private boolean deleted;
}
