package domain.common.exception;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EntityException extends RuntimeException {

    protected Class entityClass;
    protected UUID id;
}
