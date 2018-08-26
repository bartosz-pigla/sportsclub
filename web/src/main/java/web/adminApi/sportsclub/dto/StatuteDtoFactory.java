package web.adminApi.sportsclub.dto;

import query.model.sportsclub.StatuteEntity;

public final class StatuteDtoFactory {

    public static StatuteDto create(StatuteEntity statute) {
        return StatuteDto.builder()
                .id(statute.getId().toString())
                .title(statute.getTitle())
                .description(statute.getDescription())
                .build();
    }
}
