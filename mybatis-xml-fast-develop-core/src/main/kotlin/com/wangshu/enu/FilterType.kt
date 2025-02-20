package com.wangshu.enu

enum class FilterType {
    NAME, PASSWORD, PHONE, EMAIL, ID_CARD, ADDRESS, ACCOUNT, NULL;

    companion object {
        @JvmStatic
        fun fromName(name: String): DataBaseType? {
            return enumValues<DataBaseType>().find { it.name == name }
        }
    }
}

