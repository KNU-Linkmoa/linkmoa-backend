package com.knu.linkmoa.global.error.custom;

import com.knu.linkmoa.domain.sharepage.error.MemberSharePageErrorCode;
import lombok.Getter;

@Getter
public class MemberSharePageException extends RuntimeException{

    private MemberSharePageErrorCode memberSharePageErrorCode;

    public MemberSharePageException(MemberSharePageErrorCode memberSharePageErrorCode) {
        this.memberSharePageErrorCode = memberSharePageErrorCode;
    }
}
