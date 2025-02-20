package com.wangshu.enu

import com.wangshu.exception.ErrorInfo

enum class CommonErrorInfo(private var code: String?, private var msg: String?) : ErrorInfo {

    WARN("warn", "warn"),

    ERROR("-1", "服务异常"),

    SUCCESS("200", "成功"),

    BODY_NOT_MATCH("400", "参数不合法"),

    PERMISSION_ERROR("401", "您没有权限"),

    TOKEN_ERROR("401", "验证信息过期"),

    REFUSE("403", "拒绝连接"),

    NOT_FOUND("404", "资源不存在"),

    SERVER_ERROR("500", "服务器内部错误");

    override fun getResultCode(): String? {
        return code;
    }

    override fun getResultMsg(): String? {
        return msg;
    }

}

