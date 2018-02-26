package sportsclub.web.controller;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sportsclub.api.user.command.CreateUserCommand;
import sportsclub.api.validation.ValidationException;
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

//    @ExceptionHandler(ValidationException.class)
//    public void handle(ValidationException e){
//        e.getErrors();
//    }
}
