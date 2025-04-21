package com.wangshu.enu

enum class DataBaseType {
    //  oracle 先放放，要求好严格
    oracle,
    mssql,
    postgresql,
    mysql;

    companion object {
        @JvmStatic
        fun fromName(name: String): DataBaseType? {
            return enumValues<DataBaseType>().find { it.name == name }
        }
    }
}