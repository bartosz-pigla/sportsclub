package web.adminApi.user;

import static web.common.RequestMappings.ADMIN_CONSOLE_CUSTOMER;
import static web.common.RequestMappings.ADMIN_CONSOLE_DIRECTOR;
import static web.common.RequestMappings.ADMIN_CONSOLE_RECEPTIONIST;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import query.model.user.UserType;
import web.common.UserBaseController;
import web.common.dto.CreateUserWebCommand;
import web.common.service.CreateUserService;
import web.common.service.CreateUserWebCommandValidator;

@RestController
@AllArgsConstructor
final class CreateUserController extends UserBaseController {

    private CreateUserService createUserService;
    private CreateUserWebCommandValidator validator;

    @InitBinder
    private void initValidation(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @PostMapping(ADMIN_CONSOLE_CUSTOMER)
    ResponseEntity<?> createUser(@RequestBody @Validated CreateUserWebCommand customer, BindingResult bindingResult) {
        return createUser(customer, UserType.CUSTOMER, bindingResult);
    }

    @PostMapping(ADMIN_CONSOLE_DIRECTOR)
    ResponseEntity<?> createDirector(@RequestBody @Validated CreateUserWebCommand director, BindingResult bindingResult) {
        return createUser(director, UserType.DIRECTOR, bindingResult);
    }

    @PostMapping(ADMIN_CONSOLE_RECEPTIONIST)
    ResponseEntity<?> createReceptionist(@RequestBody @Validated CreateUserWebCommand receptionist, BindingResult bindingResult) {
        return createUser(receptionist, UserType.RECEPTIONIST, bindingResult);
    }

    private ResponseEntity<?> createUser(CreateUserWebCommand user, UserType userType, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validationResponseService.getResponse(bindingResult);
        }

        createUserService.create(user, userType);
        return ResponseEntity.ok(user);
    }
}
