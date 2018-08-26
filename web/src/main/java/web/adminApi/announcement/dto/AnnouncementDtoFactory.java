package web.adminApi.announcement.dto;

import query.model.announcement.AnnouncementEntity;

public final class AnnouncementDtoFactory {

    public static AnnouncementDto create(AnnouncementEntity announcement) {
        return AnnouncementDto.builder()
                .id(announcement.getId().toString())
                .title(announcement.getTitle())
                .lastModificationDate(announcement.getLastModificationDate())
                .content(announcement.getContent())
                .build();
    }
}
