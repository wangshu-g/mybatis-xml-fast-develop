package com.wangshu.generate.xml;

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