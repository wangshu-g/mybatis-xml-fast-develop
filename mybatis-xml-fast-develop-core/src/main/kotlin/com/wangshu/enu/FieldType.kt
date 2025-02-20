package com.wangshu.enu

enum class FieldType {
    BASE_FIELD, CLASS_FIELD, COLLECTION_FIELD;
    companion object {
        @JvmStatic
        fun fromName(name: String): DataBaseType? {
            return enumValues<DataBaseType>().find { it.name == name }
        }
    }
}
