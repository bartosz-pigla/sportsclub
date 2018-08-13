package web.adminApi.sportObject;

import static web.common.RequestMappings.ADMIN_CONSOLE_SPORT_OBJECT;
import static web.common.RequestMappings.ADMIN_CONSOLE_SPORT_OBJECT_BY_NAME;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;

import api.sportObject.command.CreateSportObjectCommand;
import api.sportObject.command.DeleteSportObjectCommand;
import api.sportObject.command.UpdateSportObjectCommand;
import commons.ErrorCode;
import domain.common.exception.AlreadyCreatedException;
import domain.common.exception.AlreadyDeletedException;
import domain.sportObject.exception.NotAssignedToAnySportsclubException;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import query.model.embeddable.Address;
import query.model.embeddable.City;
import query.model.embeddable.Coordinates;
import query.model.sportobject.SportObjectEntity;
import query.model.sportobject.repository.SportObjectEntityRepository;
import query.model.sportobject.repository.SportObjectQueryExpressions;
import query.model.sportsclub.SportsclubEntity;
import query.model.sportsclub.repository.SportsclubEntityRepository;
import query.model.sportsclub.repository.SportsclubQueryExpressions;
import web.adminApi.sportObject.dto.AddressDto;
import web.adminApi.sportObject.dto.SportObjectDto;
import web.adminApi.sportObject.dto.SportObjectDtoFactory;
import web.adminApi.sportObject.service.SportObjectDtoValidator;
import web.common.BaseController;
import web.common.dto.FieldErrorDto;

@RestController
@Setter(onMethod_ = { @Autowired })
final class SportObjectController extends BaseController {

    private SportObjectEntityRepository sportObjectRepository;
    private SportsclubEntityRepository sportsclubRepository;
    private SportObjectDtoValidator validator;

    @InitBinder
    private void initValidation(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @PostMapping(ADMIN_CONSOLE_SPORT_OBJECT)
    ResponseEntity<?> createSportObject(@PathVariable String sportsclubName, @RequestBody @Validated SportObjectDto sportObject, BindingResult bindingResult) throws MalformedURLException {
        if (bindingResult.hasErrors()) {
            return validationResponseService.getResponse(bindingResult);
        }

        Optional<SportsclubEntity> sportsclubOptional = sportsclubRepository.findOne(
                SportsclubQueryExpressions.nameMatches(sportsclubName));

        if (sportsclubOptional.isPresent()) {
            SportsclubEntity sportsclub = sportsclubOptional.get();
            AddressDto address = sportObject.getAddress();
            commandGateway.sendAndWait(CreateSportObjectCommand.builder()
                    .name(sportObject.getName())
                    .sportsclubId(sportsclub.getId())
                    .address(new Address(
                            address.getStreet(),
                            new City(address.getCity()),
                            new Coordinates(address.getLatitude(), address.getLongitude())))
                    .image(new URL(sportObject.getImageUrl()))
                    .description(sportObject.getDescription()).build());
            return ResponseEntity.ok(sportObject);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(ADMIN_CONSOLE_SPORT_OBJECT_BY_NAME)
    ResponseEntity<?> updateSportObject(@PathVariable String sportsclubName,
                                        @PathVariable String sportObjectName,
                                        @RequestBody @Validated SportObjectDto sportObjectDto,
                                        BindingResult bindingResult) throws MalformedURLException {
        if (bindingResult.hasErrors()) {
            return validationResponseService.getResponse(bindingResult);
        }

        Optional<SportsclubEntity> sportsclubOptional = sportsclubRepository.findOne(
                SportsclubQueryExpressions.nameMatches(sportsclubName));

        Optional<SportObjectEntity> sportObjectOptional = sportObjectRepository.findOne(
                SportObjectQueryExpressions.nameMatches(sportObjectName));

        if (sportsclubOptional.isPresent() && sportObjectOptional.isPresent()) {
            UUID sportsclubId = sportsclubOptional.get().getId();
            UUID sportObjectId = sportObjectOptional.get().getId();
            AddressDto address = sportObjectDto.getAddress();

            commandGateway.sendAndWait(UpdateSportObjectCommand.builder()
                    .sportObjectId(sportObjectId)
                    .name(sportObjectDto.getName())
                    .sportsclubId(sportsclubId)
                    .address(new Address(
                            address.getStreet(),
                            new City(address.getCity()),
                            new Coordinates(address.getLatitude(), address.getLongitude())))
                    .image(new URL(sportObjectDto.getImageUrl()))
                    .description(sportObjectDto.getDescription()).build());

            SportObjectEntity sportObject = sportObjectRepository.getOne(sportObjectId);
            return ResponseEntity.ok(SportObjectDtoFactory.create(sportObject));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(ADMIN_CONSOLE_SPORT_OBJECT_BY_NAME)
    ResponseEntity<?> deleteSportObject(@PathVariable String sportsclubName,
                                        @PathVariable String sportObjectName) {
        Optional<SportsclubEntity> sportsclubOptional = sportsclubRepository.findOne(
                SportsclubQueryExpressions.nameMatches(sportsclubName));

        Optional<SportObjectEntity> sportObjectOptional = sportObjectRepository.findOne(
                SportObjectQueryExpressions.nameMatches(sportObjectName));

        if (sportsclubOptional.isPresent() && sportObjectOptional.isPresent()) {
            SportObjectEntity sportObject = sportObjectOptional.get();
            commandGateway.sendAndWait(DeleteSportObjectCommand.builder()
                    .sportObjectId(sportObject.getId()).build());
            return ResponseEntity.ok(SportObjectDtoFactory.create(sportObject));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyCreatedException.class)
    public ResponseEntity<?> handleAlreadyCreatedConflict() {
        return validationResponseService.getResponse(
                HttpStatus.CONFLICT,
                new FieldErrorDto("name", ErrorCode.ALREADY_EXISTS.getCode()));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyDeletedException.class)
    public ResponseEntity<?> handleAlreadyDeletedConflict() {
        return validationResponseService.getResponse(
                HttpStatus.CONFLICT,
                new FieldErrorDto("name", ErrorCode.EMPTY.getCode()));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(NotAssignedToAnySportsclubException.class)
    public ResponseEntity<?> handleNotAssignedToAnySportsclubConflict() {
        return validationResponseService.getResponse(
                HttpStatus.CONFLICT,
                new FieldErrorDto("sportsclubName", ErrorCode.EMPTY.getCode()));
    }
}
