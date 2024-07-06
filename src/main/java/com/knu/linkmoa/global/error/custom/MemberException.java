package com.knu.linkmoa.global.error.custom;

import com.knu.linkmoa.domain.member.error.MemberErrorCode;
import com.knu.linkmoa.global.error.errorcode.ErrorCode;
import lombok.Getter;

@Getter
public class MemberException extends RuntimeException{

    private MemberErrorCode errorCode;

    public MemberException(MemberErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
