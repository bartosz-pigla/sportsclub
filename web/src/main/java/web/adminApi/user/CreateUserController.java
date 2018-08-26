package web.adminApi.user;

import static org.springframework.http.ResponseEntity.ok;
import static query.model.user.repository.UserQueryExpressions.usernameMatches;
import static web.common.RequestMappings.DIRECTOR_API_CUSTOMER;
import static web.common.RequestMappings.DIRECTOR_API_DIRECTOR;
import static web.common.RequestMappings.DIRECTOR_API_RECEPTIONIST;
import static web.common.user.dto.UserDtoFactory.create;

import java.util.Optional;

import commons.ErrorCode;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import query.model.user.UserEntity;
import query.model.user.UserType;
import web.adminApi.user.service.CreateUserService;
import web.common.user.UserBaseController;
import web.common.user.dto.CreateUserWebCommand;
import web.common.user.service.CreateUserWebCommandValidator;

@RestController
@Setter(onMethod_ = { @Autowired })
final class CreateUserController extends UserBaseController {

    private CreateUserService createUserService;
    private CreateUserWebCommandValidator validator;

    @InitBinder
    private void initValidation(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @PostMapping(DIRECTOR_API_CUSTOMER)
    ResponseEntity<?> createUser(@RequestBody @Validated CreateUserWebCommand customer,
                                 BindingResult bindingResult) {
        return createUser(customer, UserType.CUSTOMER, bindingResult);
    }

    @PostMapping(DIRECTOR_API_DIRECTOR)
    ResponseEntity<?> createDirector(@RequestBody @Validated CreateUserWebCommand director,
                                     BindingResult bindingResult) {
        return createUser(director, UserType.DIRECTOR, bindingResult);
    }

    @PostMapping(DIRECTOR_API_RECEPTIONIST)
    ResponseEntity<?> createReceptionist(@RequestBody @Validated CreateUserWebCommand receptionist,
                                         BindingResult bindingResult) {
        return createUser(receptionist, UserType.RECEPTIONIST, bindingResult);
    }

    private ResponseEntity<?> createUser(CreateUserWebCommand userWebCommand,
                                         UserType userType,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return errorResponseService.create(bindingResult);
        }

        createUserService.create(userWebCommand, userType);
        Optional<UserEntity> userOptional = userRepository.findOne(usernameMatches(userWebCommand.getUsername()));

        return userOptional.<ResponseEntity<?>> map(user -> ok(create(user)))
                .orElse(errorResponseService.create("username", ErrorCode.NOT_EXISTS, HttpStatus.CONFLICT));
    }
}
