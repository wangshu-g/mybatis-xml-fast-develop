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

    public IException(String errorCode, String errorMsg, Exception e) {
        this.e = e;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public IException(String errorMsg, Exception e) {
        this(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), errorMsg, e);
    }

    public IException(String errorMsg) {
        this(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), errorMsg, new RuntimeException(errorMsg));
    }

    public IException(@NotNull HttpStatus httpStatus) {
        this(String.valueOf(httpStatus.value()), httpStatus.getReasonPhrase(), new RuntimeException(httpStatus.getReasonPhrase()));
    }

    public IException(@NotNull ErrorInfo errorInfo) {
        this(String.valueOf(errorInfo.getCode()), errorInfo.getMsg(), new RuntimeException(errorInfo.getMsg()));
    }

    public IException(Exception e) {
        this(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage(), e);
    }

    @Override
    public String getMessage() {
        return this.errorMsg;
    }

}


