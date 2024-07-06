package com.knu.linkmoa.auth.itself.dto.response;

import com.knu.linkmoa.global.spec.ApiResponseSpec;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
public class AuthResultDto<T> extends ApiResponseSpec {
    private T data;

    @Builder
    public AuthResultDto(Boolean status, int code, String message, T data) {
        super(status, code, message);
        this.data = data;
    }
}