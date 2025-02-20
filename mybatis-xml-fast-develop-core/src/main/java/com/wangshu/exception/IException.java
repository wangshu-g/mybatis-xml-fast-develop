package com.wangshu.exception;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

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

    public IException(@NotNull ErrorInfo errorInfoInterface) {
        this.errorCode = errorInfoInterface.getResultCode();
        this.errorMsg = errorInfoInterface.getResultMsg();
        this.e = new RuntimeException(errorMsg);
    }

    public IException(String errorCode, String errorMsg, Exception e) {
        this.e = e;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public IException(String errorMsg, Exception e) {
        this.e = e;
        this.errorCode = "warn";
        this.errorMsg = errorMsg;
    }

    public IException(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.e = new RuntimeException(errorMsg);
    }

    public IException(String errorMsg) {
        this.errorMsg = errorMsg;
        this.errorCode = "warn";
        this.e = new RuntimeException(errorMsg);
    }

    @Override
    public String getMessage() {
        return this.errorMsg;
    }
}


