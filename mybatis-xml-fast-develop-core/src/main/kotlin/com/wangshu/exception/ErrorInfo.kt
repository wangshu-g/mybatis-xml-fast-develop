package com.wangshu.exception

interface ErrorInfo {

    fun getResultCode(): String?

    fun getResultMsg(): String?

}