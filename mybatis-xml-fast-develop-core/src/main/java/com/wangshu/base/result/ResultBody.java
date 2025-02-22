package com.wangshu.base.result;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.Filter;
import com.wangshu.exception.ErrorInfo;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>响应数据格式</p>
 */
@Data
public class ResultBody<T> implements Serializable {

    /**
     * <p>响应结果</p>
     **/
    T data;

    /**
     * <p>响应代码</p>
     **/
    String code;

    /**
     * <p>提示信息</p>
     **/
    String message;

    boolean status;

    public ResultBody() {
    }

    /**
     * @param data    响应数据
     * @param message 提示信息
     * @return ResultBody
     **/
    @NotNull
    public static <T> ResultBody<T> build(T data, String code, String message, boolean status) {
        ResultBody<T> resultBody = new ResultBody<>();
        resultBody.setData(data);
        resultBody.setCode(code);
        resultBody.setMessage(message);
        resultBody.setStatus(status);
        return resultBody;
    }

    /**
     * <p>成功</p>
     *
     * @return ResultBody
     **/
    @NotNull
    public static <T> ResultBody<T> success() {
        return ResultBody.success(null);
    }

    /**
     * <p>成功</p>
     *
     * @param data 响应数据
     * @return ResultBody
     **/
    @NotNull
    public static <T> ResultBody<T> success(T data) {
        return ResultBody.success(data, HttpStatus.OK.getReasonPhrase());
    }

    @NotNull
    public static <T> ResultBody<T> successMsg(String message) {
        return ResultBody.success(null, message);
    }

    /**
     * <p>成功</p>
     *
     * @param data    响应数据
     * @param message 提示信息
     * @return ResultBody
     **/
    @NotNull
    public static <T> ResultBody<T> success(T data, String message) {
        return ResultBody.success(data, String.valueOf(HttpStatus.OK.value()), message);
    }

    /**
     * <p>成功</p>
     *
     * @param data    响应数据
     * @param message 提示信息
     * @return ResultBody
     **/
    @NotNull
    public static <T> ResultBody<T> success(T data, String code, String message) {
        return ResultBody.build(data, code, message, true);
    }

    /**
     * <p>失败</p>
     *
     * @param message 提示信息
     * @return ResultBody
     **/
    @NotNull
    public static <T> ResultBody<T> error(String message) {
        return ResultBody.error(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), message);
    }

    @NotNull
    public static <T> ResultBody<T> error(@NotNull HttpStatus httpStatus) {
        return ResultBody.error(String.valueOf(httpStatus.value()), httpStatus.getReasonPhrase());
    }

    @NotNull
    public static <T> ResultBody<T> error(@NotNull ErrorInfo errorInfo) {
        return ResultBody.error(String.valueOf(errorInfo.getCode()), errorInfo.getMsg());
    }

    /**
     * <p>失败</p>
     *
     * @param code    响应码
     * @param message 提示信息
     * @return ResultBody
     **/
    @NotNull
    public static <T> ResultBody<T> error(String code, String message) {
        return ResultBody.build(null, code, message, false);
    }

    public String toJson() {
        return JSON.toJSONString(this);
    }

    public String toJson(JSONWriter.Context context) {
        return JSON.toJSONString(this, context);
    }

    public String toJson(JSONWriter.Feature... features) {
        return JSON.toJSONString(this, features);
    }

    public String toJson(Filter filter, JSONWriter.Feature... features) {
        return JSON.toJSONString(this, filter, features);
    }

    public String toJson(Filter[] filters, JSONWriter.Feature... features) {
        return JSON.toJSONString(this, filters, features);
    }

    public String toJson(String format, JSONWriter.Feature[] features) {
        return JSON.toJSONString(this, format, features);
    }

    public String toJson(String format, Filter[] filters, JSONWriter.Feature[] features) {
        return JSON.toJSONString(this, format, filters, features);
    }

    public String toJsonDateFormat(String format) {
        return this.toJson(format, new JSONWriter.Feature[]{});
    }

    public String toJsonyMdHms() {
        return this.toJsonDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public String toJsonyMd() {
        return this.toJsonDateFormat("yyyy-MM-dd");
    }

    public Map<String, Object> toMap() {
        List<Field> fields = new ArrayList<>();
        Class<?> clazz = this.getClass();
        while (clazz != null) {
            fields.addAll(List.of(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        Map<String, Object> map = new HashMap<>(fields.size());
        Logger log = LoggerFactory.getLogger(this.getClass());
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(this));
            } catch (IllegalAccessException e) {
                log.error("无法访问的字段", e);
            }
        }
        return map;
    }

}
