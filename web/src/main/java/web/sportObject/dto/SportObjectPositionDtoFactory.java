package web.sportObject.dto;

import query.model.sportobject.SportObjectPositionEntity;

public final class SportObjectPositionDtoFactory {

    public static SportObjectPositionDto create(SportObjectPositionEntity position) {
        return SportObjectPositionDto.builder()
                .id(position.getId().toString())
                .name(position.getName())
                .description(position.getDescription())
                .positionsCount(position.getPositionsCount().getPositionsCount())
                .build();
    }
}
