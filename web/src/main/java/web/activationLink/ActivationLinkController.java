package web.activationLink;

import static web.common.RequestMappings.ACTIVATION_LINK;

import api.user.command.SendActivationLinkCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import query.model.user.UserEntity;
import query.repository.UserEntityRepository;
import web.common.BaseController;
import web.common.ValidationResponseService;

@RestController(ACTIVATION_LINK)
final class ActivationLinkController extends BaseController {

    private UserEntityRepository userEntityRepository;

    public ActivationLinkController(CommandGateway commandGateway, ValidationResponseService validationResponseService, UserEntityRepository userEntityRepository) {
        super(commandGateway, validationResponseService);
        this.userEntityRepository = userEntityRepository;
    }

    @GetMapping
    public ResponseEntity<?> get(){
        UserEntity userEntity = userEntityRepository.findByUsername("customer1").get();
        commandGateway.send(new SendActivationLinkCommand(userEntity.getId()));
        return ResponseEntity.ok(new Object());
    }
}
