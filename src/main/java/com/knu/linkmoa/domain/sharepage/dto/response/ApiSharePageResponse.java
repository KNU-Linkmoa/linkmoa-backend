package com.knu.linkmoa.domain.sharepage.dto.response;

import com.knu.linkmoa.global.spec.ApiResponseSpec;

public class ApiSharePageResponse<T> extends ApiResponseSpec {

    private T data;

    public ApiSharePageResponse(Boolean status, int code, String message, T data) {
        super(status, code, message);
        this.data = data;
    }
}
