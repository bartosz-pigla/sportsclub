package web.signUp;

import static web.common.RequestMappings.CUSTOMER_ACTIVATION;

import java.util.Optional;
import java.util.UUID;

import api.user.command.ActivateCustomerCommand;
import domain.user.activateCustomer.exception.ActivationKeyInvalidException;
import domain.user.activateCustomer.exception.ActivationLinkExpiredException;
import domain.user.activateCustomer.exception.AlreadyActivatedException;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import query.model.user.ActivationLinkEntry;
import query.model.user.UserEntity;
import query.repository.ActivationLinkEntryRepository;
import web.common.BaseController;
import web.common.FieldErrorDto;
import web.signUp.dto.ActivateCustomerWebCommand;

@RestController
@Setter(onMethod_ = { @Autowired })
final class CustomerActivationController extends BaseController {

    private ActivationLinkEntryRepository activationLinkRepository;

    @PostMapping(CUSTOMER_ACTIVATION)
    ResponseEntity<?> activateCustomer(@RequestBody ActivateCustomerWebCommand activateCustomerCommand) {
        Optional<ActivationLinkEntry> activationLinkOptional = activationLinkRepository
                .findById(UUID.fromString(activateCustomerCommand.getActivationKey()));

        if (activationLinkOptional.isPresent()) {
            UserEntity customer = activationLinkOptional.get().getUser();
            commandGateway.sendAndWait(ActivateCustomerCommand.builder()
                    .customerId(customer.getId())
                    .activationKey(UUID.fromString(activateCustomerCommand.getActivationKey())).build());
            return ResponseEntity.ok(customer.getUsername());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ActivationLinkExpiredException.class)
    public ResponseEntity<?> handleActivationKeyExpiredConflict() {
        return validationResponseService.getResponse(
                HttpStatus.CONFLICT,
                new FieldErrorDto("activationKey", "expired"));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ActivationKeyInvalidException.class)
    public ResponseEntity<?> handleActivationKeyInvalidConflict() {
        return validationResponseService.getResponse(
                HttpStatus.CONFLICT,
                new FieldErrorDto("activationKey", "invalid"));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyActivatedException.class)
    public ResponseEntity<?> handleCustomerAlreadyActivatedConflict() {
        return validationResponseService.getResponse(
                HttpStatus.CONFLICT,
                new FieldErrorDto("activationKey", "alreadyActivated"));
    }
}
