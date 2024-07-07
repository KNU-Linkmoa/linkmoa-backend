package com.knu.linkmoa.domain.sharepage.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum MemberSharePageErrorCode {

    MEMBER_SHARE_PAGE_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "해당 유저 혹은 페이지 번호에 해당하는 공유 페이지가 없습니다"),
    ALREADY_SHARE_PAGE_ACCEPT_EXCEPTION(HttpStatus.BAD_REQUEST, "해당 유저는 이미 공유페이지에 참가하였습니다");

    private final HttpStatus httpStatus;
    private final String message;
}
