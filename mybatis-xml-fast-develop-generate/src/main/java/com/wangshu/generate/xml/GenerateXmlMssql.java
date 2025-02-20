package com.wangshu.generate.xml;

import cn.hutool.core.util.StrUtil;
import com.wangshu.exception.MessageException;
import com.wangshu.generate.metadata.field.ColumnInfo;
import com.wangshu.generate.metadata.model.ModelInfo;
import org.dom4j.Element;

import java.util.function.Consumer;

public class GenerateXmlMssql<T extends ModelInfo<?, F>, F extends ColumnInfo<?, T>> extends GenerateXml<T, F> {

    public GenerateXmlMssql(T model) {
        super(model);
    }

    public GenerateXmlMssql(T model, Consumer<MessageException> message) {
        super(model, message);
    }

    @Override
    public String getLikeIfText(String tableName, String columnName, String testConditionName, boolean isAndStr) {
        String orAndStr = isAndStr ? "and " : "or ";
        return StrUtil.concat(false, orAndStr, "charindex(", this.getPreCompileStr(testConditionName), ",", this.getBackQuoteStr(tableName), ".", this.getBackQuoteStr(columnName), ") != 0");
    }

    @Override
    public String getBackQuoteStr(String str) {
        return StrUtil.concat(false, "\"", str, "\"");
    }

    @Override
    public Element getLimit() {
        org.dom4j.Element ifElement = this.createXmlElement("if");
        ifElement.addAttribute("test", "pageIndex != null and pageSize != null");
        ifElement.addText(StrUtil.concat(false, "offset ", this.getPreCompileStr("pageIndex"), " rows fetch next ", this.getPreCompileStr("pageSize"), " rows only"));
        return ifElement;
    }
}