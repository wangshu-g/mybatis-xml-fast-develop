package com.wangshu.enu

enum class FieldType {
    BASE_FIELD, CLASS_FIELD, COLLECTION_FIELD;

    companion object {
        @JvmStatic
        fun fromName(name: String): FieldType? {
            return enumValues<FieldType>().find { it.name == name }
        }
    }
}
