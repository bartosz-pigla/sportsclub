package web.signIn;

import lombok.Data;

@Data
final class SignInWebCommand {

    private String username;
    private String password;
}
