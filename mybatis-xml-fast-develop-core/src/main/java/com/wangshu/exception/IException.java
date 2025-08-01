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

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

/**
 * @author wangshu-g
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

    public IException(String errorCode, String errorMsg) {
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


