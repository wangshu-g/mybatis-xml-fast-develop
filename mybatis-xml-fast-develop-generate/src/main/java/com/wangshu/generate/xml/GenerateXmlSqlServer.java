package com.wangshu.generate.xml;

import com.wangshu.exception.MessageException;
import com.wangshu.generate.metadata.field.ColumnInfo;
import com.wangshu.generate.metadata.model.ModelInfo;
import com.wangshu.tool.StringUtil;
import org.dom4j.Element;

import java.util.function.Consumer;

public class GenerateXmlSqlServer<T extends ModelInfo<?, F>, F extends ColumnInfo<?, T>> extends GenerateXml<T, F> {

    public GenerateXmlSqlServer(T model) {
        super(model);
    }

    public GenerateXmlSqlServer(T model, Consumer<MessageException> message) {
        super(model, message);
    }

    @Override
    public String getLikeIfText(String tableName, String columnName, String testConditionName, boolean isAndStr) {
        String orAndStr = isAndStr ? "and " : "or ";
        return StringUtil.concat(orAndStr, "charindex(", this.getPreCompileStr(testConditionName), ",", this.getBackQuoteStr(tableName), ".", this.getBackQuoteStr(columnName), ") != 0");
    }

    @Override
    public String getBackQuoteStr(String str) {
        return StringUtil.concat("\"", str, "\"");
    }

    @Override
    public Element getLimit() {
        org.dom4j.Element ifElement = this.createXmlElement("if");
        ifElement.addAttribute("test", "pageIndex != null and pageSize != null");
        ifElement.addText(StringUtil.concat("offset ", this.getPreCompileStr("pageIndex"), " rows fetch next ", this.getPreCompileStr("pageSize"), " rows only"));
        return ifElement;
    }
}