package query.model.announcement.repository;

import static query.model.baseEntity.repository.BaseEntityQueryExpressions.isNotDeleted;

import java.util.Optional;
import java.util.UUID;

import com.querydsl.core.types.dsl.BooleanExpression;
import query.model.announcement.QAnnouncementEntity;
import query.model.baseEntity.repository.BaseEntityQueryExpressions;

public final class AnnouncementQueryExpressions {

    private static final QAnnouncementEntity announcement = QAnnouncementEntity.announcementEntity;

    public static BooleanExpression titleMatches(String title) {
        return Optional.ofNullable(title)
                .map(n -> isNotDeleted(announcement._super).and(announcement.title.eq(title)))
                .orElse(announcement.isNull());
    }

    public static BooleanExpression idMatches(UUID id) {
        return BaseEntityQueryExpressions.idMatches(id, announcement._super);
    }
}
