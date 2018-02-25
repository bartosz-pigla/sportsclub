package sportsclub.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import sportsclub.api.user.command.CreateUserCommand;
import sportsclub.domain.user.model.UserEntry;

@Service
public class CreateUserValidator {
    private Errors errors;
    private UserEntryRepository userEntryRepository;

    public CreateUserValidator(Errors errors, UserEntryRepository userEntryRepository) {
        this.errors = errors;
        this.userEntryRepository = userEntryRepository;
    }

    public void validate(CreateUserCommand object) {

    }
}
