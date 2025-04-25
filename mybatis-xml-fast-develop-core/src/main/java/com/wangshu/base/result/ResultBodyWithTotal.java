package com.wangshu.base.result;

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

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

/**
 * @author wangshu-g
 * <p>返回给表格用的数据格式</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ResultBodyWithTotal<T> extends ResultBody<T> {

    int total = 0;

    public ResultBodyWithTotal() {
    }

    @NotNull
    public static <T> ResultBodyWithTotal<T> build(T data, int total, String code, String message, boolean status) {
        ResultBodyWithTotal<T> resultBodyWithTotal = new ResultBodyWithTotal<>();
        resultBodyWithTotal.setData(data);
        resultBodyWithTotal.setCode(code);
        resultBodyWithTotal.setMessage(message);
        resultBodyWithTotal.setStatus(status);
        resultBodyWithTotal.setTotal(total);
        return resultBodyWithTotal;
    }

    public static <T> @NotNull ResultBodyWithTotal<T> success(T data, int total) {
        return ResultBodyWithTotal.build(data, total, String.valueOf(HttpStatus.OK.value()), HttpStatus.OK.getReasonPhrase(), true);
    }

}
