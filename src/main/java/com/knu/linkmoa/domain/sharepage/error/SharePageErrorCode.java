package com.knu.linkmoa.domain.sharepage.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SharePageErrorCode {

    SHARE_PAGE_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "Share Page를 찾을 수 없습니다");

    private final HttpStatus httpStatus;
    private final String message;

}
