package web.adminApi.sportObject.dto;

import query.model.sportobject.SportObjectPositionEntity;

public final class SportObjectPositionDtoFactory {

    public static SportObjectPositionDto create(SportObjectPositionEntity sportObjectPositionEntity) {
        return SportObjectPositionDto.builder()
                .name(sportObjectPositionEntity.getName())
                .description(sportObjectPositionEntity.getDescription())
                .positionsCount(sportObjectPositionEntity.getPositionsCount().getPositionsCount())
                .build();
    }
}
