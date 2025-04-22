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
import com.wangshu.enu.JoinCondition;
import com.wangshu.enu.JoinType;
import com.wangshu.enu.SqlStyle;
import com.wangshu.exception.MessageException;
import com.wangshu.generate.metadata.field.ColumnInfo;
import com.wangshu.generate.metadata.model.ModelInfo;
import com.wangshu.tool.CommonStaticField;
import org.dom4j.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GenerateXmlOracle<T extends ModelInfo<?, F>, F extends ColumnInfo<?, T>> extends GenerateXml<T, F> {

    public GenerateXmlOracle(T model) {
        super(model);
    }

    public GenerateXmlOracle(T model, @Nullable Consumer<MessageException> messageExceptionConsumer) {
        super(model, messageExceptionConsumer);
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

    @Override
    public Element generateSave() {
        F primaryField = this.getModel().getPrimaryField();
        org.dom4j.Element insertElement = this.createXmlElement("insert");
        insertElement.addAttribute("id", CommonStaticField.SAVE_METHOD_NAME);
        insertElement.addAttribute("parameterType", "Map");
        List<F> baseFields = this.getModel().getBaseFields();
        if (StrUtil.equals(primaryField.getJavaTypeName(), Long.class.getName()) || StrUtil.equals(primaryField.getJavaTypeName(), Integer.class.getName())) {
            insertElement.addAttribute("useGeneratedKeys", "true");
            insertElement.addAttribute("keyProperty", primaryField.getName());
            baseFields = baseFields.stream().filter(item -> !item.equals(primaryField)).toList();
        }
        insertElement.addText(StrUtil.concat(false,
                CommonStaticField.WRAP,
                "insert into ",
                this.getBackQuoteStr(this.getModel().getTableName()),
                "(",
                baseFields.stream().map(item -> getBackQuoteStr(item.getSqlStyleName())).collect(Collectors.joining(",")),
                ") values (",
                String.join(",", baseFields.stream().map(item -> getPreCompileStr(StrUtil.concat(false, item.getName(), ",jdbcType=", item.getMybatisJdbcType().name())))
                        .collect(Collectors.joining(","))),
                ")",
                CommonStaticField.WRAP));
        return insertElement;
    }

    @Override
    public Element generateBatchSave() {
        F primaryField = this.getModel().getPrimaryField();
        org.dom4j.Element batchInsertElement = this.createXmlElement("insert");
        batchInsertElement.addAttribute("id", CommonStaticField.BATCH_SAVE_METHOD_NAME);
        batchInsertElement.addAttribute("parameterType", "List");
        List<F> baseFields = this.getModel().getBaseFields();
        if (StrUtil.equals(primaryField.getJavaTypeName(), Long.class.getName()) || StrUtil.equals(primaryField.getJavaTypeName(), Integer.class.getName())) {
            batchInsertElement.addAttribute("useGeneratedKeys", "true");
            batchInsertElement.addAttribute("keyProperty", primaryField.getName());
            baseFields = baseFields.stream().filter(item -> !item.equals(primaryField)).toList();
        }
        String text = StrUtil.concat(false, CommonStaticField.WRAP, "insert into ", this.getBackQuoteStr(this.getModel().getTableName()), "(", baseFields.stream().map(item -> getBackQuoteStr(item.getSqlStyleName())).collect(Collectors.joining(",")), ") values", CommonStaticField.WRAP);
        batchInsertElement.addText(text);
        String forEachText = StrUtil.concat(false,
                "(",
                String.join(",", baseFields.stream().map(item -> getPreCompileStr(StrUtil.concat(false, "item.", item.getName(), ",jdbcType=", item.getMybatisJdbcType().name())))
                        .collect(Collectors.joining(","))),
                ")");
        org.dom4j.Element forEachElement = this.getForEachElement("list", null, null, null, null, null);
        forEachElement.addText(forEachText);
        batchInsertElement.add(forEachElement);
        return batchInsertElement;
    }

    @Override
    public @NotNull List<String> getFieldsJoinTextList(@NotNull List<F> fields) {
        List<String> fieldsJoinText = new ArrayList<>();
        for (F field : fields) {
            if (field.isClassJoinField()) {
                T leftModel = field.getLeftModel();
                String leftJoinField = Objects.equals(leftModel.getSqlStyle(), SqlStyle.lcc) ? StrUtil.lowerFirst(field.getLeftJoinField()) : StrUtil.toUnderlineCase(field.getLeftJoinField());
                String leftTable = leftModel.getTableName();
                String leftTableAs = this.getJoinLeftTableAsName(field);

                T rightModel = field.getRightModel();
                String rightJoinField = Objects.equals(rightModel.getSqlStyle(), SqlStyle.lcc) ? StrUtil.lowerFirst(field.getRightJoinField()) : StrUtil.toUnderlineCase(field.getRightJoinField());
                String rightTable = rightModel.getTableName();
                String rightTableAs = this.getJoinRightTableAsName(field);

                JoinType joinType = field.getJoinType();
                JoinCondition joinCondition = field.getJoinCondition();

                fieldsJoinText.add(StrUtil.concat(false, joinType.name(), " join ", this.getBackQuoteStr(leftTable), " ", this.getBackQuoteStr(leftTableAs), " on ", this.getBackQuoteStr(leftTableAs), ".", this.getBackQuoteStr(leftJoinField), " = ", this.getBackQuoteStr(rightTableAs), ".", this.getBackQuoteStr(rightJoinField)));
            }
        }
        return fieldsJoinText;
    }

    @Override
    public Element generateUpdate() {
        org.dom4j.Element updateElement = this.createXmlElement("update");
        updateElement.addAttribute("id", CommonStaticField.UPDATE_METHOD_NAME);
        updateElement.addAttribute("parameterType", "Map");
        String text = StrUtil.concat(false, CommonStaticField.WRAP, "update ", this.getBackQuoteStr(this.getModel().getTableName()), CommonStaticField.WRAP);
        updateElement.addText(text);
        org.dom4j.Element setElement = this.createXmlElement("set");
        this.getModel().getBaseFields().forEach(item -> {
            Element ifNotNullElement = this.getIfNotNullElement(StrUtil.concat(false, "new", StrUtil.upperFirst(item.getName())));
            ifNotNullElement.addText(StrUtil.concat(false,
                    getBackQuoteStr(item.getSqlStyleName()),
                    " = ",
                    getPreCompileStr(StrUtil.concat(false, "new", StrUtil.upperFirst(item.getName()), ",jdbcType=", item.getMybatisJdbcType().name())), ","));
            setElement.add(ifNotNullElement);
            Element ifSetNullElement = this.getIfSetNullElement(item.getName());
            ifSetNullElement.addText(StrUtil.concat(false, getBackQuoteStr(item.getSqlStyleName()), " = null,"));
            setElement.add(ifSetNullElement);
        });
        updateElement.add(setElement);
        org.dom4j.Element whereElement = this.createXmlElement("where");

        List<F> tempFields = new ArrayList<>(this.getModel().getBaseFields());
        this.getIf(tempFields, whereElement::add);

        updateElement.add(whereElement);
        return updateElement;
    }

}