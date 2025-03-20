package com.wangshu.base.result;

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
