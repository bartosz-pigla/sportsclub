package domain.common;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EntityException extends RuntimeException {

    protected Class entityClass;
    protected UUID id;
}
