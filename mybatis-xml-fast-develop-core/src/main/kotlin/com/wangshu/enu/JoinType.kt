package com.wangshu.enu

enum class JoinType {
    left, right, inner;

    companion object {
        @JvmStatic
        fun fromName(name: String): JoinType? {
            return enumValues<JoinType>().find { it.name == name }
        }
    }
}