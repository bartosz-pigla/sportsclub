package query.model.announcement.repository;

import static query.model.baseEntity.repository.BaseEntityQueryExpressions.isNotDeleted;

import com.querydsl.core.types.dsl.BooleanExpression;
import query.model.announcement.QAnnouncementEntity;

public final class AnnouncementQueryExpressions {

    private static final QAnnouncementEntity announcement = QAnnouncementEntity.announcementEntity;

    public BooleanExpression titleMatches(String title) {
        return isNotDeleted(announcement._super).and(announcement.title.eq(title));
    }
}
