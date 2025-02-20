package com.wangshu.exception;

import com.wangshu.base.result.ResultBody;
import com.wangshu.enu.CommonErrorInfo;
import com.wangshu.tool.CommonParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

/**
 * <p>异常处理及错误日志记录</p>
 */
@Slf4j
@ControllerAdvice
public class IExceptionHandler {

    @ExceptionHandler(IException.class)
    @ResponseBody
    public String iExceptionHandler(IException e) {
        log.error("错误接口: {}", Objects.requireNonNull(CommonParam.getRequest()).getServletPath());
        log.error("错误信息", e);
        return ResultBody.error(e.getErrorCode(), e.getMessage()).toJson();
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String exceptionHandler(Exception e) {
        log.error("错误接口: {}", Objects.requireNonNull(CommonParam.getRequest()).getServletPath());
        log.error("错误信息", e);
        if (e instanceof ErrorResponse errorResponse) {
            return ResultBody.error(String.valueOf(errorResponse.getStatusCode().value()), e.getMessage()).toJsonyMdHms();
        }
        return ResultBody.error(CommonErrorInfo.SERVER_ERROR).toJson();
    }

}


