package web.controller;

import javax.validation.Valid;

import api.user.command.CreateUserCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.model.AggregateNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import web.config.RequestMappings;

@RestController
public class UserController {

    private CommandGateway commandGateway;

    public UserController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @GetMapping(RequestMappings.foo)
    public String foo() {
        commandGateway.sendAndWait(new CreateUserCommand("login", "password"));
        return "foo";
    }

    @PostMapping(RequestMappings.createUser)
    public void createUser(@RequestBody @Valid CreateUserCommand command) {
        commandGateway.sendAndWait(command);
    }

    @ExceptionHandler(AggregateNotFoundException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handle(AggregateNotFoundException e) {
        return "USER NOT FOUND";
    }
}
