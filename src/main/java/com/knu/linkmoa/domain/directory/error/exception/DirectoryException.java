package com.knu.linkmoa.domain.directory.error.exception;

import com.knu.linkmoa.domain.directory.error.errorcode.DirectoryErrorCode;
import lombok.Getter;

@Getter
public class DirectoryException extends RuntimeException {

    private DirectoryErrorCode directoryErrorCode;

    public DirectoryException(DirectoryErrorCode directoryErrorCode) {

        this.directoryErrorCode = directoryErrorCode;
    }
}
