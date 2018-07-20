package domain.user.exception;

import api.user.command.ActivateCustomerCommand;

public final class AlreadyActivatedException extends CustomerActivationException {

    public AlreadyActivatedException(ActivateCustomerCommand command) {
        super(command);
    }
}
