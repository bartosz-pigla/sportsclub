package web.common.dto;

import commons.ErrorCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class FieldErrorDto {

    private String field;
    private String code;

    public FieldErrorDto(String field, ErrorCode code) {
        this.field = field;
        this.code = code.getCode();
    }

    public FieldErrorDto(String field, String code) {
        this.field = field;
        this.code = code;
    }
}
