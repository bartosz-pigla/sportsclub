package sportsclub.domain.user.service;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import sportsclub.api.user.command.ActivateUserCommand;
import sportsclub.api.user.command.CreateUserCommand;
import sportsclub.domain.user.model.UserEntry;

@Service
@AllArgsConstructor
public class UserValidator {
    private UserEntryRepository userEntryRepository;
    private MessageSource messageSource;

    public Errors validate(CreateUserCommand command) {
        Errors errors = null;
        UserEntry user = userEntryRepository.findOne(command.getLogin());
        if (user != null) {
            errors = new BeanPropertyBindingResult(command, CreateUserCommand.class.getSimpleName());
            errors.reject("login", messageSource.getMessage("user.alreadyExists", null, LocaleContextHolder.getLocale()));
        }
        return errors;
    }

    public Errors validate(ActivateUserCommand command) {
        Errors errors = null;
        UserEntry notActivatedUser = userEntryRepository.findOne(command.getLogin());
        if (notActivatedUser.isActivated()) {
            errors = new BeanPropertyBindingResult(command, CreateUserCommand.class.getSimpleName());
            errors.reject("login", messageSource.getMessage("user.alreadyActivated", null, LocaleContextHolder.getLocale()));
        }
        return errors;
    }

}
