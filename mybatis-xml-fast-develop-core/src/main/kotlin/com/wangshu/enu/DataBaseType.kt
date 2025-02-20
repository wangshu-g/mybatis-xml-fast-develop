package com.wangshu.enu

enum class DataBaseType {
    oracle, sqlServer, mysql, postgres;

    companion object {
        @JvmStatic
        fun fromName(name: String): DataBaseType? {
            return enumValues<DataBaseType>().find { it.name == name }
        }

    }
}