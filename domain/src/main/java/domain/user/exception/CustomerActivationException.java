package domain.user.exception;

import api.user.command.ActivateCustomerCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomerActivationException extends RuntimeException {

    protected ActivateCustomerCommand command;
}
