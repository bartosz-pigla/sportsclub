package sportsclub.domain.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import sportsclub.api.user.command.ActivateUserCommand;
import sportsclub.api.user.command.CreateUserCommand;
import sportsclub.domain.user.model.UserEntry;

@Service
@AllArgsConstructor
public class UserValidator {

    private UserEntryRepository userEntryRepository;

    public void validate(CreateUserCommand command, Errors errors) {
        UserEntry user = userEntryRepository.findOne(command.getLogin());
        if (user != null) {
            errors.rejectValue("login", "user.alreadyExists");
        }
    }

    public void validate(ActivateUserCommand command, Errors errors) {
        UserEntry notActivatedUser = userEntryRepository.findOne(command.getLogin());
        if (notActivatedUser.isActivated()) {
            errors.rejectValue("activated", "user.alreadyActivated");
        }
    }
}
