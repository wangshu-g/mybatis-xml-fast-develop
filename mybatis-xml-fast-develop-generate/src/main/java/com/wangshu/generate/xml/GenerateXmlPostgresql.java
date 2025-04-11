package com.wangshu.generate.xml;

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

import cn.hutool.core.util.StrUtil;
import com.wangshu.exception.MessageException;
import com.wangshu.generate.metadata.field.ColumnInfo;
import com.wangshu.generate.metadata.model.ModelInfo;

import java.util.function.Consumer;

public class GenerateXmlPostgresql<T extends ModelInfo<?, F>, F extends ColumnInfo<?, T>> extends GenerateXml<T, F> {

    public GenerateXmlPostgresql(T model) {
        super(model);
    }

    public GenerateXmlPostgresql(T model, Consumer<MessageException> message) {
        super(model, message);
    }

    @Override
    public String getLikeIfText(String tableName, String columnName, String testConditionName, boolean isAndStr) {
        String orAndStr = isAndStr ? "and " : "or ";
        return StrUtil.concat(false, orAndStr, "strpos(", this.getBackQuoteStr(tableName), ".", this.getBackQuoteStr(columnName), ",", this.getPreCompileStr(testConditionName), ") > 0");
    }

    @Override
    public String getBackQuoteStr(String str) {
        return StrUtil.concat(false, "\"", str, "\"");
    }
}