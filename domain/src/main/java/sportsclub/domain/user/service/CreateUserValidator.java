package sportsclub.domain.user.service;

import org.springframework.stereotype.Service;
import sportsclub.api.user.CreateUserCommand;

@Service
public class CreateUserValidator {
    public void validate(CreateUserCommand object) {
        System.out.println("VALID CREATEUSERCOMMAND");
    }
}
