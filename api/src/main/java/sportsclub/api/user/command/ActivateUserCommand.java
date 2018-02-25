package sportsclub.api.user.command;

import lombok.Value;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

import javax.validation.constraints.NotNull;

@Value
public class ActivateUserCommand {
    @TargetAggregateIdentifier
    @NotNull
    String login;
}
