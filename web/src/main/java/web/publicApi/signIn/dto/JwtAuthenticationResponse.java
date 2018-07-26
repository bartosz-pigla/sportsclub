package web.publicApi.signIn.dto;

import static java.lang.String.format;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public final class JwtAuthenticationResponse {

    private String accessToken;
    private String tokenType = "Bearer";

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return format("%s %s", tokenType, accessToken);
    }
}
