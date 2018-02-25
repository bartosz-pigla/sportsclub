package sportsclub.api.user.command;

import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
public final class CreateUserCommand {
    @NotNull
    String login;
    @NotNull
    String password;
}
