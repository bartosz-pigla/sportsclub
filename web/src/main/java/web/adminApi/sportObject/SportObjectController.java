package web.adminApi.sportObject;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import static web.common.RequestMappings.DIRECTOR_API_SPORT_OBJECT;
import static web.common.RequestMappings.DIRECTOR_API_SPORT_OBJECT_BY_ID;

import java.util.List;
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
import query.model.embeddable.ImageUrl;
import query.model.sportobject.repository.SportObjectEntityRepository;
import query.model.sportobject.repository.SportObjectQueryExpressions;
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

    @PostMapping(DIRECTOR_API_SPORT_OBJECT)
    ResponseEntity<?> create(@PathVariable UUID sportsclubId,
                             @RequestBody @Validated SportObjectDto sportObjectDto,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return errorResponseService.create(bindingResult);
        }

        boolean sportsclubExists = sportsclubRepository.exists(
                SportsclubQueryExpressions.idMatches(sportsclubId));

        if (sportsclubExists) {
            AddressDto address = sportObjectDto.getAddress();
            String name = sportObjectDto.getName();

            commandGateway.sendAndWait(CreateSportObjectCommand.builder()
                    .name(name)
                    .sportsclubId(sportsclubId)
                    .address(new Address(
                            address.getStreet(),
                            new City(address.getCity()),
                            new Coordinates(address.getLatitude(), address.getLongitude())))
                    .imageUrl(new ImageUrl(sportObjectDto.getImageUrl()))
                    .description(sportObjectDto.getDescription())
                    .build());

            return sportObjectRepository.findOne(SportObjectQueryExpressions.nameAndSportsclubIdMatches(name, sportsclubId))
                    .<ResponseEntity<?>> map(sportObject -> ok(SportObjectDtoFactory.create(sportObject)))
                    .orElse(errorResponseService.create("id", ErrorCode.NOT_EXISTS, HttpStatus.CONFLICT));
        } else {
            return badRequest().build();
        }
    }

    @PutMapping(DIRECTOR_API_SPORT_OBJECT_BY_ID)
    ResponseEntity<?> update(@PathVariable UUID sportsclubId,
                             @PathVariable UUID sportObjectId,
                             @RequestBody @Validated SportObjectDto sportObjectDto,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return errorResponseService.create(bindingResult);
        }

        AddressDto address = sportObjectDto.getAddress();

        Runnable sendCommand = () -> commandGateway.sendAndWait(UpdateSportObjectCommand.builder()
                .sportObjectId(sportObjectId)
                .name(sportObjectDto.getName())
                .sportsclubId(sportsclubId)
                .address(new Address(
                        address.getStreet(),
                        new City(address.getCity()),
                        new Coordinates(address.getLatitude(), address.getLongitude())))
                .imageUrl(new ImageUrl(sportObjectDto.getImageUrl()))
                .description(sportObjectDto.getDescription())
                .build());

        boolean commandSent = sendCommand(sportsclubId, sportObjectId, sendCommand);
        return commandSent ? noContent().build() : badRequest().build();
    }

    @DeleteMapping(DIRECTOR_API_SPORT_OBJECT_BY_ID)
    ResponseEntity<?> delete(@PathVariable UUID sportsclubId,
                             @PathVariable UUID sportObjectId) {
        Runnable sendCommand = () -> commandGateway.sendAndWait(
                new DeleteSportObjectCommand(sportObjectId));

        boolean commandSent = sendCommand(sportsclubId, sportObjectId, sendCommand);
        return commandSent ? noContent().build() : badRequest().build();
    }

    private boolean sendCommand(UUID sportsclubId, UUID sportObjectId, Runnable sendCommand) {
        boolean sportsclubExists = sportsclubRepository.exists(
                SportsclubQueryExpressions.idMatches(sportsclubId));

        boolean sportObjectExists = sportObjectRepository.exists(
                SportObjectQueryExpressions.idMatches(sportObjectId));

        if (sportsclubExists && sportObjectExists) {
            sendCommand.run();
            return true;
        } else {
            return false;
        }
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyCreatedException.class)
    public List<FieldErrorDto> handleAlreadyCreatedConflict() {
        return errorResponseService.createBody("name", ErrorCode.ALREADY_EXISTS);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyDeletedException.class)
    public List<FieldErrorDto> handleAlreadyDeletedConflict() {
        return errorResponseService.createBody("id", ErrorCode.EMPTY);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(NotAssignedToAnySportsclubException.class)
    public List<FieldErrorDto> handleNotAssignedToAnySportsclubConflict() {
        return errorResponseService.createBody("sportsclubId", ErrorCode.EMPTY);
    }
}
