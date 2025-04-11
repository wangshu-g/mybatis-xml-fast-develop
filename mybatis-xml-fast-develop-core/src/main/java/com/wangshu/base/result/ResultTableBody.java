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

import com.wangshu.cache.ColumnType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * @author wangshu-g
 * <p>返回给表格用的数据格式</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ResultTableBody<T> extends ResultBody<T> {

    List<ColumnType> columns;

    int total = 0;

    public ResultTableBody() {
    }

    @NotNull
    public static <T> ResultTableBody<T> build(T data, int total, List<ColumnType> columns, String code, String message, boolean status) {
        ResultTableBody<T> resultTableBody = new ResultTableBody<>();
        resultTableBody.setData(data);
        resultTableBody.setCode(code);
        resultTableBody.setMessage(message);
        resultTableBody.setStatus(status);
        resultTableBody.setTotal(total);
        resultTableBody.setColumns(columns);
        return resultTableBody;
    }

    public static <T> @NotNull ResultTableBody<T> success(T data, int total, List<ColumnType> columns) {
        return ResultTableBody.build(data, total, columns, String.valueOf(HttpStatus.OK.value()), HttpStatus.OK.getReasonPhrase(), true);
    }

    public static <T> @NotNull ResultTableBody<T> success(T data, int total) {
        return ResultTableBody.build(data, total, List.of(), String.valueOf(HttpStatus.OK.value()), HttpStatus.OK.getReasonPhrase(), true);
    }

}
