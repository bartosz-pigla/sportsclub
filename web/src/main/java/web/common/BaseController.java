package web.common;

import lombok.Setter;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import web.common.service.ErrorResponseService;

@Setter(onMethod_ = { @Autowired })
public abstract class BaseController {

    protected CommandGateway commandGateway;
    protected ErrorResponseService errorResponseService;

    protected boolean isInvalidUUID(String uuid) {
        return uuid.split("-").length != 5;
    }
}
