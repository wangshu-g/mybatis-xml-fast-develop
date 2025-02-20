package com.wangshu.enu

/**
 * 几种条件,默认and连接,带or的or连接
 *

 **/
enum class Condition {
    equal, less, great, like, `in`, isNull, isNotNull, orEqual, orLess, orGreat, orLike, orIn, orIsNull, orIsNotNull, all;

    companion object {
        @JvmStatic
        fun fromName(name: String): DataBaseType? {
            return enumValues<DataBaseType>().find { it.name == name }
        }
    }
}
