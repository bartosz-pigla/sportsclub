package web.sportObject;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static query.model.sportobject.repository.SportObjectQueryExpressions.sportsclubIdMatches;
import static web.common.RequestMappings.PUBLIC_API_SPORT_OBJECT;

import java.util.List;
import java.util.UUID;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import query.model.sportobject.repository.SportObjectEntityRepository;
import web.common.BaseController;
import web.sportObject.dto.SportObjectDto;
import web.sportObject.dto.SportObjectDtoFactory;

@RestController
@Setter(onMethod_ = { @Autowired })
final class SportObjectPublicController extends BaseController {

    private SportObjectEntityRepository sportObjectRepository;

    @GetMapping(PUBLIC_API_SPORT_OBJECT)
    @ResponseStatus(HttpStatus.OK)
    List<SportObjectDto> get(@PathVariable UUID sportsclubId) {
        return stream(sportObjectRepository.findAll(sportsclubIdMatches(sportsclubId)).spliterator(), false)
                .map(SportObjectDtoFactory::create)
                .collect(toList());
    }

}
