package web.adminApi.user;

import static org.springframework.http.ResponseEntity.ok;
import static web.common.RequestMappings.ADMIN_API_CUSTOMER;
import static web.common.RequestMappings.ADMIN_API_DIRECTOR;
import static web.common.RequestMappings.ADMIN_API_RECEPTIONIST;

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
import web.adminApi.user.service.CreateUserService;
import web.common.user.UserBaseController;
import web.common.user.dto.CreateUserWebCommand;
import web.common.user.service.CreateUserWebCommandValidator;

@RestController
@AllArgsConstructor
final class CreateUserController extends UserBaseController {

    private CreateUserService createUserService;
    private CreateUserWebCommandValidator validator;

    @InitBinder
    private void initValidation(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @PostMapping(ADMIN_API_CUSTOMER)
    ResponseEntity<?> createUser(@RequestBody @Validated CreateUserWebCommand customer, BindingResult bindingResult) {
        return createUser(customer, UserType.CUSTOMER, bindingResult);
    }

    @PostMapping(ADMIN_API_DIRECTOR)
    ResponseEntity<?> createDirector(@RequestBody @Validated CreateUserWebCommand director, BindingResult bindingResult) {
        return createUser(director, UserType.DIRECTOR, bindingResult);
    }

    @PostMapping(ADMIN_API_RECEPTIONIST)
    ResponseEntity<?> createReceptionist(@RequestBody @Validated CreateUserWebCommand receptionist, BindingResult bindingResult) {
        return createUser(receptionist, UserType.RECEPTIONIST, bindingResult);
    }

    private ResponseEntity<?> createUser(CreateUserWebCommand user, UserType userType, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validationResponseService.getResponse(bindingResult);
        }

        createUserService.create(user, userType);
        return ok(user);
    }
}
