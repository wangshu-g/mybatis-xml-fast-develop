package com.wangshu.exception;

// MIT License
//
// Copyright (c) 2025 2560334673@qq.com wangshu-g https://github.com/wangshu-g/mybatis-xml-fast-develop
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

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
        log.error("错误信息: ", e);
        return ResultBody.error(e.getErrorCode(), e.getMessage()).toJson();
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String exceptionHandler(Exception e) {
        HttpServletRequest request = CommonParam.getRequest();
        if (Objects.nonNull(request)) {
            log.error("错误接口: {}", request.getServletPath());
        }
        log.error("错误信息: ", e);
        if (e instanceof ErrorResponse errorResponse) {
            return ResultBody.error(String.valueOf(errorResponse.getStatusCode().value()), e.getMessage()).toJsonyMdHms();
        }
        return ResultBody.error(HttpStatus.INTERNAL_SERVER_ERROR).toJson();
    }

}


