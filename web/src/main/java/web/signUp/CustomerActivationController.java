package web.signUp;

import static java.util.UUID.fromString;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.noContent;
import static query.model.user.repository.ActivationLinkQueryExpressions.customerIdMatches;
import static query.model.user.repository.ActivationLinkQueryExpressions.idMatches;
import static web.common.RequestMappings.PUBLIC_API_CUSTOMER_ACTIVATE;

import java.util.List;
import java.util.UUID;

import api.user.command.ActivateCustomerCommand;
import commons.ErrorCode;
import domain.user.activation.common.exception.AlreadyActivatedException;
import domain.user.activation.customer.exception.ActivationLinkExpiredException;
import domain.user.activation.customer.exception.ActivationLinkInvalidException;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import query.model.user.repository.ActivationLinkEntryRepository;
import web.common.dto.FieldErrorDto;
import web.user.UserBaseController;
import web.signUp.dto.ActivateCustomerWebCommand;

@RestController
@Setter(onMethod_ = { @Autowired })
final class CustomerActivationController extends UserBaseController {

    private ActivationLinkEntryRepository activationLinkRepository;

    @PatchMapping(PUBLIC_API_CUSTOMER_ACTIVATE)
    ResponseEntity<?> activateCustomer(
            @PathVariable UUID userId,
            @RequestBody ActivateCustomerWebCommand activateCustomerCommand) {
        String activationKeyStr = activateCustomerCommand.getActivationKey();

        if (isInvalidUUID(activationKeyStr)) {
            return badRequest().build();
        }

        UUID activationKey = fromString(activationKeyStr);

        boolean activationLinkExists = activationLinkRepository.exists(
                idMatches(activationKey).and(customerIdMatches(userId)));

        if (activationLinkExists) {
            commandGateway.sendAndWait(new ActivateCustomerCommand(userId, activationKey));
            return noContent().build();
        } else {
            return badRequest().build();
        }
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ActivationLinkExpiredException.class)
    public List<FieldErrorDto> handleActivationKeyExpiredConflict() {
        return errorResponseService.createBody("activationKey", ErrorCode.EXPIRED);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ActivationLinkInvalidException.class)
    public List<FieldErrorDto> handleActivationKeyInvalidConflict() {
        return errorResponseService.createBody("activationKey", ErrorCode.INVALID);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyActivatedException.class)
    public List<FieldErrorDto> handleCustomerAlreadyActivatedConflict() {
        return errorResponseService.createBody("activationKey", ErrorCode.ALREADY_ACTIVATED);
    }
}
