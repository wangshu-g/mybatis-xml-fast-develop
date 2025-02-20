package com.wangshu.enu

enum class JoinType {
    left, right, inner;

    companion object {
        @JvmStatic
        fun fromName(name: String): DataBaseType? {
            return enumValues<DataBaseType>().find { it.name == name }
        }
    }
}