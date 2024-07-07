package com.knu.linkmoa.domain.sharepage.dto.response;

import com.knu.linkmoa.global.spec.ApiResponseSpec;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ApiSharePageResponse<T> extends ApiResponseSpec {

    private T data;

    @Builder
    public ApiSharePageResponse(Boolean status, int code, String message, T data) {
        super(status, code, message);
        this.data = data;
    }
}
