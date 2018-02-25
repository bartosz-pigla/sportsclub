package sportsclub.api.user;

import javax.validation.constraints.NotNull;

public final class CreateUserCommand {
    @NotNull
    private String login;
    @NotNull
    private String password;

    public CreateUserCommand() {
    }

    public CreateUserCommand(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
