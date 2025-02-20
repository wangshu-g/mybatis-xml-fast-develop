package com.wangshu.filter;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.filter.ValueFilter;
import com.wangshu.annotation.FastJsonFilter;
import com.wangshu.enu.FilterType;
import com.wangshu.tool.CommonTool;
import com.wangshu.tool.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * <p>fastjson过滤敏感数据实现,可以放在响应拦截注册使用</p>
 */
@Slf4j
public class FastJsonValueFilter implements ValueFilter {

    @Override
    public Object apply(@NotNull Object object, String name, Object value) {
        Field clazzFieldByName = CommonTool.getClazzFieldByName(object.getClass(), name);
        if (Objects.nonNull(clazzFieldByName) && clazzFieldByName.isAnnotationPresent(FastJsonFilter.class)) {
            value = format(clazzFieldByName.getAnnotation(FastJsonFilter.class).filterType(), value);
        }
        return value;
    }

    @Nullable
    private Object format(@NotNull FilterType filterType, Object object) {
        if (!(object instanceof String value)) {
            log.error("字段不是字符串类型: {}", object);
            return null;
        }
        if (filterType.equals(FilterType.NULL) || StrUtil.isEmpty(value)) {
            return null;
        }
        switch (filterType) {
            case NAME ->
                    value = Validator.isChineseName(value) ? DesensitizedUtil.chineseName(value) : StringUtil.concat(value.substring(0, 1), "**");
            case PASSWORD -> value = "******";
            case PHONE -> value = PhoneUtil.isPhone(value) ? DesensitizedUtil.mobilePhone(value) : null;
            case EMAIL -> value = Validator.isEmail(value) ? DesensitizedUtil.email(value) : null;
            case ID_CARD -> value = IdcardUtil.isValidCard(value) ? DesensitizedUtil.idCardNum(value, 1, 2) : null;
            case ADDRESS -> value = DesensitizedUtil.address(value, 8);
            case ACCOUNT -> value = value.length() >= 2 ? StrUtil.hide(value, 1, 1) : null;
        }
        return value;
    }

}
