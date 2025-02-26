package com.wangshu.enu

/**
 * sql 风格
 *
 **/
enum class SqlStyle {

    //    Lower Camel Case
    lcc,

    //    Snake Case
    sc;

    companion object {
        @JvmStatic
        fun fromName(name: String): SqlStyle? {
            return enumValues<SqlStyle>().find { it.name == name }
        }
    }
}
