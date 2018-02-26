package sportsclub.web.controller;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.model.AggregateNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sportsclub.api.user.command.CreateUserCommand;
import sportsclub.web.config.RequestMappings;

import javax.validation.Valid;

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
