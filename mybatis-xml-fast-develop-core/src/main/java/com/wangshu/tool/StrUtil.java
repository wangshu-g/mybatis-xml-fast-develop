package com.wangshu.tool;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public final class StrUtil {

    private StrUtil() {
    }

    public static boolean isBlankIfStr(Object obj) {
        return Objects.toString(obj, "").isBlank();
    }

    public static boolean isBlank(String str) {
        return StringUtils.isBlank(str);
    }

    public static boolean isNotBlank(String str) {
        return StringUtils.isNotBlank(str);
    }

    public static boolean equals(String str1, String str2) {
        return StringUtils.equals(str1, str2);
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        return StringUtils.equalsIgnoreCase(str1, str2);
    }

    public static String upperFirst(String str) {
        return StringUtils.capitalize(str);
    }

    public static String lowerFirst(String str) {
        return StringUtils.uncapitalize(str);
    }

    public static String concat(boolean ignoreNull, String... strs) {
        if (Objects.isNull(strs) || strs.length == 0) {
            return "";
        }
        if (ignoreNull) {
            return StringUtils.join(strs, "");
        } else {
            String[] temp = new String[strs.length];
            for (int i = 0; i < strs.length; i++) {
                temp[i] = Objects.isNull(strs[i]) ? "" : strs[i];
            }
            return StringUtils.join(temp, "");
        }
    }

    public static String toUnderlineCase(String str) {
        if (StringUtils.isBlank(str)) return str;
        StringBuilder sb = new StringBuilder();
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (Character.isUpperCase(c)) {
                if (i != 0) sb.append('_');
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

}