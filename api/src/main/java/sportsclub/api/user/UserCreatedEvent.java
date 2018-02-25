package sportsclub.api.user;

public class UserCreatedEvent {
    private String login;
    private String password;

    public UserCreatedEvent() {
    }

    public UserCreatedEvent(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
