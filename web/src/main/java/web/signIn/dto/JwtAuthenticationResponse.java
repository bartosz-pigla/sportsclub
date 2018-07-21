package web.signIn.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class JwtAuthenticationResponse {

    private String accessToken;
    private String tokenType = "Bearer";

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
