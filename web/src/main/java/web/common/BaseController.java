package web.common;

import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;

@AllArgsConstructor
public abstract class BaseController {

    protected CommandGateway commandGateway;
    protected ValidationResponseService validationResponseService;
}
