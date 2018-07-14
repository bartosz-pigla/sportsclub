package domain.common;

import java.util.UUID;

public final class EntityAlreadyExistsException extends EntityException {

    public EntityAlreadyExistsException(Class entityClass, UUID id) {
        super(entityClass, id);
    }
}
