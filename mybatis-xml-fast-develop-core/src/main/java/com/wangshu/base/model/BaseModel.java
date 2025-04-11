package com.wangshu.base.model;

// MIT License
//
// Copyright (c) 2025 2560334673@qq.com wangshu-g https://github.com/wangshu-g/mybatis-xml-fast-develop
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.wangshu.annotation.Column;
import com.wangshu.base.entity.EntityTool;
import com.wangshu.tool.CacheTool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author wangshu-g
 * <p>BaseModel</p>
 */
public class BaseModel extends EntityTool {

    @Override
    public List<Field> modelFields() {
        return CacheTool.getModelFields(this.getClass());
    }

    public @Nullable Field modelPrimaryField() {
        return CacheTool.getModelPrimaryField(this.getClass());
    }

    public void setModelValuesFromMapByFieldNameWithTitle(@NotNull Map<String, Object> map) {
        Map<String, Field> fields = this.modelFieldsMap();
        fields.forEach((fieldName, field) -> {
            String title = fieldName;
            Column annotation = field.getAnnotation(Column.class);
            if (Objects.nonNull(annotation) && StrUtil.isNotBlank(annotation.title())) {
                title = annotation.title();
            }
            if (Objects.nonNull(map.get(title))) {
                field.setAccessible(true);
                try {
                    field.set(this, Convert.convert(field.getType(), map.get(title)));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

}
