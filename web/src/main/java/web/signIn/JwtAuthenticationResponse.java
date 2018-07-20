package web.signIn;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
final class JwtAuthenticationResponse {

    private String accessToken;
    private String tokenType = "Bearer";

    JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
