package com.wangshu.generate.xml;

import com.wangshu.exception.MessageException;
import com.wangshu.generate.metadata.field.ColumnInfo;
import com.wangshu.generate.metadata.model.ModelInfo;
import com.wangshu.tool.StringUtil;

import java.util.function.Consumer;

public class GenerateXmlPostgres<T extends ModelInfo<?, F>, F extends ColumnInfo<?, T>> extends GenerateXml<T, F> {

    public GenerateXmlPostgres(T model) {
        super(model);
    }

    public GenerateXmlPostgres(T model, Consumer<MessageException> message) {
        super(model, message);
    }

    @Override
    public String getLikeIfText(String tableName, String columnName, String testConditionName, boolean isAndStr) {
        String orAndStr = isAndStr ? "and " : "or ";
        return StringUtil.concat(orAndStr, "strpos(", this.getBackQuoteStr(tableName), ".", this.getBackQuoteStr(columnName), ",", this.getPreCompileStr(testConditionName), ") > 0");
    }

    @Override
    public String getBackQuoteStr(String str) {
        return StringUtil.concat("\"", str, "\"");
    }
}