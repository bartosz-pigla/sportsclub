package web.signIn.dto;

import static java.lang.String.format;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import web.user.dto.UserDto;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtAuthenticationResponse {

    private String accessToken;
    private UserDto user;
    private String tokenType = "Bearer";

    public JwtAuthenticationResponse(String accessToken, UserDto user) {
        this.accessToken = accessToken;
        this.user = user;
    }

    @Override
    public String toString() {
        return format("%s %s", tokenType, accessToken);
    }
}
