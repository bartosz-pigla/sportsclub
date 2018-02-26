package sportsclub.api.user.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserCommand {
    @NotNull
    String login;
    @NotNull
    String password;
}
