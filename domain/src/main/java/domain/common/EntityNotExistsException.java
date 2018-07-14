package domain.common;

import java.util.UUID;

public final class EntityNotExistsException extends EntityException {

    public EntityNotExistsException(Class entityClass, UUID id) {
        super(entityClass, id);
    }
}
