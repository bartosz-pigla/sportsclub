package domain.user.exception;

import api.user.command.ActivateCustomerCommand;

public class ActivationLinkExpiredException extends CustomerActivationException {

    public ActivationLinkExpiredException(ActivateCustomerCommand command) {
        super(command);
    }
}
