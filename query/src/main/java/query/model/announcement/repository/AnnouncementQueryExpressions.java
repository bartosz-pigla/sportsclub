package query.model.announcement.repository;

import static query.model.baseEntity.repository.BaseEntityQueryExpressions.isNotDeleted;

import com.querydsl.core.types.dsl.BooleanExpression;
import query.model.announcement.QAnnouncementEntity;

public final class AnnouncementQueryExpressions {

    public BooleanExpression titleMatches(String title) {
        return isNotDeleted().and(QAnnouncementEntity.announcementEntity.title.eq(title));
    }
}
