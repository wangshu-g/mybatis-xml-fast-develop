package com.wangshu.exception;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

/**
 * @author GSF
 * <p>全局统一异常</p>
 */
@Getter
public class IException extends RuntimeException {

    private String errorCode;

    private String errorMsg;

    private Exception e;

    public IException() {
        super();
    }

    public IException(@NotNull HttpStatus httpStatus) {
        this.errorCode = String.valueOf(httpStatus.value());
        this.errorMsg = httpStatus.getReasonPhrase();
        this.e = new RuntimeException(errorMsg);
    }

    public IException(String errorCode, String errorMsg, Exception e) {
        this.e = e;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    @Override
    public String getMessage() {
        return this.errorMsg;
    }

}


