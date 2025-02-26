package com.wangshu.enu

enum class JoinCondition {
    equal, less, great, like;

    companion object {
        @JvmStatic
        fun fromName(name: String): JoinCondition? {
            return enumValues<JoinCondition>().find { it.name == name }
        }
    }
}
