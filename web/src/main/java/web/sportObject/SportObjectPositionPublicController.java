package web.sportObject;

import static query.model.sportobject.repository.SportObjectPositionQueryExpressions.sportObjectIdMatches;
import static web.common.RequestMappings.PUBLIC_API_SPORT_OBJECT_POSITION;
import static web.sportObject.dto.SportObjectPositionDtoFactory.create;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import query.model.sportobject.repository.SportObjectPositionEntityRepository;
import web.sportObject.dto.SportObjectPositionDto;

@RestController
@Setter(onMethod_ = { @Autowired })
final class SportObjectPositionPublicController {

    private SportObjectPositionEntityRepository sportObjectPositionRepository;

    @GetMapping(PUBLIC_API_SPORT_OBJECT_POSITION)
    List<SportObjectPositionDto> get(@PathVariable UUID sportObjectId) {
        List<SportObjectPositionDto> positions = new ArrayList<>();
        sportObjectPositionRepository
                .findAll(sportObjectIdMatches(sportObjectId))
                .forEach(p -> positions.add(create(p)));
        return positions;
    }
}
