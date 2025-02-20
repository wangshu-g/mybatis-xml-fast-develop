package com.wangshu.enu

enum class DataBaseType {
    oracle, mssql, mysql, postgresql;

    companion object {
        @JvmStatic
        fun fromName(name: String): DataBaseType? {
            return enumValues<DataBaseType>().find { it.name == name }
        }
    }
}