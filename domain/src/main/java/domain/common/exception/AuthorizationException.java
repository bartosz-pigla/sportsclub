package domain.common.exception;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class AuthorizationException extends RuntimeException {

    private UUID resourceId;
    private UUID userId;
}
