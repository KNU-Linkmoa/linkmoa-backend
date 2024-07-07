package com.knu.linkmoa.global.error.custom;

import com.knu.linkmoa.domain.sharepage.error.SharePageErrorCode;
import lombok.Getter;

@Getter
public class SharePageException extends RuntimeException{

    private SharePageErrorCode sharePageErrorCode;

    public SharePageException(SharePageErrorCode sharePageErrorCode) {
        this.sharePageErrorCode = sharePageErrorCode;
    }
}
