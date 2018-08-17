package web.publicApi.signUp;

import static java.util.UUID.fromString;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;
import static web.common.RequestMappings.CUSTOMER_ACTIVATION;

import java.util.List;
import java.util.Optional;

import api.user.command.ActivateCustomerCommand;
import com.google.common.collect.ImmutableList;
import commons.ErrorCode;
import domain.user.activation.common.exception.AlreadyActivatedException;
import domain.user.activation.customer.exception.ActivationLinkExpiredException;
import domain.user.activation.customer.exception.ActivationLinkInvalidException;
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
import query.model.user.repository.ActivationLinkEntryRepository;
import query.model.user.repository.ActivationLinkQueryExpressions;
import web.common.dto.FieldErrorDto;
import web.common.user.UserBaseController;
import web.publicApi.signUp.dto.ActivateCustomerWebCommand;

@RestController
@Setter(onMethod_ = { @Autowired })
final class CustomerActivationController extends UserBaseController {

    private ActivationLinkEntryRepository activationLinkRepository;

    @PostMapping(CUSTOMER_ACTIVATION)
    ResponseEntity<?> activateCustomer(@RequestBody ActivateCustomerWebCommand activateCustomerCommand) {
        Optional<ActivationLinkEntry> activationLinkOptional = activationLinkRepository
                .findOne(ActivationLinkQueryExpressions.idMatches(fromString(activateCustomerCommand.getActivationKey())));

        if (activationLinkOptional.isPresent()) {
            UserEntity customer = activationLinkOptional.get().getCustomer();
            commandGateway.sendAndWait(ActivateCustomerCommand.builder()
                    .customerId(customer.getId())
                    .activationKey(fromString(activateCustomerCommand.getActivationKey())).build());
            return ok(customer.getUsername());
        } else {
            return badRequest().build();
        }
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ActivationLinkExpiredException.class)
    public List<FieldErrorDto> handleActivationKeyExpiredConflict() {
        return ImmutableList.of(new FieldErrorDto("activationKey", ErrorCode.EXPIRED));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ActivationLinkInvalidException.class)
    public List<FieldErrorDto> handleActivationKeyInvalidConflict() {
        return ImmutableList.of(new FieldErrorDto("activationKey", ErrorCode.INVALID));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyActivatedException.class)
    public List<FieldErrorDto> handleCustomerAlreadyActivatedConflict() {
        return ImmutableList.of(new FieldErrorDto("activationKey", ErrorCode.ALREADY_ACTIVATED));
    }
}
