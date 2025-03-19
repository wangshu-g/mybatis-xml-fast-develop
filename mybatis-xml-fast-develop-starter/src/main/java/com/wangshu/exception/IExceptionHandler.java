package com.wangshu.exception;

import com.wangshu.base.result.ResultBody;
import com.wangshu.tool.CommonParam;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
        HttpServletRequest request = CommonParam.getRequest();
        if (Objects.nonNull(request)) {
            log.error("错误接口: {}", request.getServletPath());
        }
        if (Objects.isNull(e.getE())) {
            log.error("错误信息", e);
        } else {
            log.error("错误信息", e.getE());
        }
        return ResultBody.error(e.getErrorCode(), e.getMessage()).toJson();
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String exceptionHandler(Exception e) {
        HttpServletRequest request = CommonParam.getRequest();
        if (Objects.nonNull(request)) {
            log.error("错误接口: {}", request.getServletPath());
        }
        log.error("错误信息", e);
        if (e instanceof ErrorResponse errorResponse) {
            return ResultBody.error(String.valueOf(errorResponse.getStatusCode().value()), e.getMessage()).toJsonyMdHms();
        }
        return ResultBody.error(HttpStatus.INTERNAL_SERVER_ERROR).toJson();
    }

}


