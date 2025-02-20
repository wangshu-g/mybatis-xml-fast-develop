package com.wangshu.enu

enum class JoinCondition {
    equal, less, great, like;

    companion object {
        @JvmStatic
        fun fromName(name: String): DataBaseType? {
            return enumValues<DataBaseType>().find { it.name == name }
        }
    }
}
