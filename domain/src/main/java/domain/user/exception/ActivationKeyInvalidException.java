package domain.user.exception;

import api.user.command.ActivateCustomerCommand;

public class ActivationKeyInvalidException extends CustomerActivationException {

    public ActivationKeyInvalidException(ActivateCustomerCommand command) {
        super(command);
    }
}
