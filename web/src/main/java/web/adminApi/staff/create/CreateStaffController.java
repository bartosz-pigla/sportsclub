package web.adminApi.staff.create;

import static web.common.RequestMappings.ADMIN_CONSOLE_DIRECTOR;
import static web.common.RequestMappings.ADMIN_CONSOLE_RECEPTIONIST;

import commons.ErrorCode;
import domain.user.createUser.exception.UserCreationException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import query.model.user.UserType;
import web.common.dto.CreateUserWebCommand;
import web.common.dto.FieldErrorDto;
import web.common.service.CreateUserService;
import web.common.service.CreateUserWebCommandValidator;
import web.common.service.ValidationResponseService;

@RestController
@AllArgsConstructor
final class CreateStaffController {

    private ValidationResponseService validationResponseService;
    private CreateUserService createUserService;
    private CreateUserWebCommandValidator validator;

    @InitBinder
    private void initValidation(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @PostMapping(ADMIN_CONSOLE_DIRECTOR)
    ResponseEntity<?> createDirector(@RequestBody @Validated CreateUserWebCommand director, BindingResult bindingResult) {
        return createStaffMember(director, UserType.DIRECTOR, bindingResult);
    }

    @PostMapping(ADMIN_CONSOLE_RECEPTIONIST)
    ResponseEntity<?> createReceptionist(@RequestBody @Validated CreateUserWebCommand receptionist, BindingResult bindingResult) {
        return createStaffMember(receptionist, UserType.RECEPTIONIST, bindingResult);
    }

    private ResponseEntity<?> createStaffMember(CreateUserWebCommand employee, UserType userType, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validationResponseService.getResponse(bindingResult);
        }

        createUserService.create(employee, userType);
        return ResponseEntity.ok(employee);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserCreationException.class)
    public ResponseEntity<?> handleUserAlreadyExistsConflict() {
        return validationResponseService.getResponse(
                HttpStatus.CONFLICT,
                new FieldErrorDto("username", ErrorCode.ALREADY_EXISTS.getCode()));
    }
}
