package commons;

import static java.lang.String.format;

import lombok.Getter;

@Getter
public enum ErrorCode {

    INVALID("invalid"),
    ALREADY_EXISTS("alreadyExists"),
    NOT_EXISTS("notExists"),
    ALREADY_ACTIVATED("alreadyActivated"),
    ALREADY_DEACTIVATED("alreadyDeactivated"),
    ALREADY_DELETED("alreadyDeleted"),
    EXPIRED("expired"),
    EMPTY("empty"),
    MAX_LENGTH("maxLength"),
    IS_NOT_ALPHA("isNotAlpha"),
    OUT_OF_RANGE("outOfRange"),
    NOT_POSITIVE("notPositive"),
    ALREADY_BOOKED("alreadyBooked"),
    LIMIT_EXCEEDED("limitExceeded");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String getCodeWithPrefix(String prefix) {
        return format("%s.%s", prefix, code);
    }
}
