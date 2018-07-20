package web.signUp;

import static web.common.RequestMappings.CUSTOMER_ACTIVATION;

import java.util.Optional;
import java.util.UUID;

import api.user.command.ActivateCustomerCommand;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import query.model.user.ActivationLinkEntry;
import query.model.user.UserEntity;
import query.repository.ActivationLinkEntryRepository;
import web.common.BaseController;
import web.signIn.UserPrincipal;

@RestController
@Setter(onMethod_ = { @Autowired })
final class CustomerActivationController extends BaseController {

    private ActivationLinkEntryRepository activationLinkRepository;

    @PostMapping(CUSTOMER_ACTIVATION)
    ResponseEntity<?> activateCustomer(@RequestBody ActivateCustomerWebCommand activateCustomerCommand, @AuthenticationPrincipal UserPrincipal currentUser) {
        Optional<ActivationLinkEntry> activationLinkOptional = activationLinkRepository
                .findById(UUID.fromString(activateCustomerCommand.getActivationKey()));

        if (activationLinkOptional.isPresent()) {
            UserEntity customer = activationLinkOptional.get().getUser();
            commandGateway.sendAndWait(ActivateCustomerCommand.builder()
                    .customerId(customer.getId())
                    .activationKey(UUID.fromString(activateCustomerCommand.getActivationKey())));
            return ResponseEntity.ok(customer.getUsername());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}
