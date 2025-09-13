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
import com.wangshu.tool.CommonStaticField;
import org.dom4j.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class GenerateXmlPostgresql<T extends ModelInfo<?, F>, F extends ColumnInfo<?, T>> extends GenerateXml<T, F> {

    public GenerateXmlPostgresql(T model) {
        super(model);
    }

    public GenerateXmlPostgresql(T model, @Nullable Consumer<MessageException> message) {
        super(model, message);
    }

    @Override
    public String getIfInstrText(String tableName, String columnName, String testConditionName, boolean isAndStr) {
        String orAndStr = isAndStr ? "and " : "or ";
        return StrUtil.concat(false, orAndStr, "strpos(", this.wrapEscapeCharacter(tableName), ".", this.wrapEscapeCharacter(columnName), ",", this.wrapMybatisPrecompileStr(testConditionName), ") > 0");
    }

    @Override
    public String wrapEscapeCharacter(String str) {
        return StrUtil.concat(false, "\"", str, "\"");
    }

    @Override
    public Element getLimit() {
        org.dom4j.Element ifElement = this.createXmlElement("if");
        ifElement.addAttribute("test", "pageIndex != null and pageSize != null");
        ifElement.addText(StrUtil.concat(false, "limit ", this.wrapMybatisPrecompileStr("pageSize"), " offset ", this.wrapMybatisPrecompileStr("pageIndex")));
        return ifElement;
    }

    private String getSequenceIdSql(@NotNull F primaryField) {
        return StrUtil.concat(false, "select nextval('",
                this.wrapEscapeCharacter(this.getPrimaryIncrSequenceName()),
                "') as ",
                primaryField.getSqlStyleName());
    }

    private String getPrimaryIncrSequenceName() {
        return StrUtil.concat(false, this.getModel().getTableName(), "_id_seq");
    }

    private @NotNull Element getSelectKeyElement(@NotNull F primaryField) {
        org.dom4j.Element selectKeyElement = this.createXmlElement("selectKey");
        selectKeyElement.addAttribute("keyProperty", primaryField.getName());
        selectKeyElement.addAttribute("resultType", primaryField.getJavaTypeName());
        selectKeyElement.addAttribute("order", "BEFORE");
        selectKeyElement.addText(this.getSequenceIdSql(primaryField));
        return selectKeyElement;
    }

    @Override
    public Element generateSave() {
        F primaryField = this.getModel().getPrimaryField();
        org.dom4j.Element insertElement = this.createXmlElement("insert");
        insertElement.addAttribute("id", CommonStaticField.SAVE_METHOD_NAME);
        insertElement.addAttribute("parameterType", "Map");
        List<F> baseFields = this.getModel().getBaseFields();
        if (primaryField.isIncrPrimary()) {
            insertElement.addAttribute("keyProperty", primaryField.getName());
            insertElement.add(this.getSelectKeyElement(primaryField));
            baseFields = baseFields.stream().filter(item -> !item.isPrimaryField()).toList();
        }
        insertElement.addText(StrUtil.concat(false,
                CommonStaticField.BREAK_WRAP,
                "insert into ",
                this.wrapEscapeCharacter(this.getModel().getTableName()),
                this.getInsertIntoColumnsText(baseFields),
                " values ",
                this.getInsertIntoValuesText(baseFields),
                CommonStaticField.BREAK_WRAP));
        return insertElement;
    }

    @Override
    public Element generateBatchSave() {
        F primaryField = this.getModel().getPrimaryField();
        org.dom4j.Element batchInsertElement = this.createXmlElement("insert");
        batchInsertElement.addAttribute("id", CommonStaticField.BATCH_SAVE_METHOD_NAME);
        batchInsertElement.addAttribute("parameterType", "List");
        List<F> baseFields = this.getModel().getBaseFields();
        if (primaryField.isIncrPrimary()) {
            batchInsertElement.addAttribute("keyProperty", primaryField.getName());
            baseFields = baseFields.stream().filter(item -> !item.isPrimaryField()).toList();
        }
        String text = StrUtil.concat(false,
                CommonStaticField.BREAK_WRAP,
                "insert into ",
                this.wrapEscapeCharacter(this.getModel().getTableName()),
                this.getInsertIntoColumnsText(baseFields),
                " values ",
                CommonStaticField.BREAK_WRAP);
        batchInsertElement.addText(text);
        org.dom4j.Element forEachElement = this.getForEachElement("list", null, null, null, null, null);
        forEachElement.addText(this.getInsertIntoForEachValuesText(baseFields));
        batchInsertElement.add(forEachElement);
        return batchInsertElement;
    }

}