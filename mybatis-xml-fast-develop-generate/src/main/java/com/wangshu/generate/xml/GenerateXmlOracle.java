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
import com.wangshu.exception.MessageException;
import com.wangshu.generate.metadata.field.ColumnInfo;
import com.wangshu.generate.metadata.model.ModelInfo;
import com.wangshu.tool.CommonStaticField;
import com.wangshu.tool.CommonTool;
import org.dom4j.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GenerateXmlOracle<T extends ModelInfo<?, F>, F extends ColumnInfo<?, T>> extends GenerateXmlWithGenerateGetSequenceId<T, F> {

    public GenerateXmlOracle(T model) {
        super(model);
    }

    public GenerateXmlOracle(T model, @Nullable Consumer<MessageException> messageExceptionConsumer) {
        super(model, messageExceptionConsumer);
    }

    @Override
    public String wrapEscapeCharacter(String str) {
        return StrUtil.concat(false, "\"", str, "\"");
    }

    @Override
    public Element getLimit() {
        org.dom4j.Element ifElement = this.createXmlElement("if");
        ifElement.addAttribute("test", "pageIndex != null and pageSize != null");
        ifElement.addText(StrUtil.concat(false, "offset ", this.wrapMybatisPrecompileStr("pageIndex"), " rows fetch next ", this.wrapMybatisPrecompileStr("pageSize"), " rows only"));
        return ifElement;
    }

    @Override
    public Element generateGetSequenceId() {
        return null;
    }

    private String getPrimaryIncrSeqName() {
        String primaryIncrSeq = CommonTool.getNewStrBySqlStyle(this.getModel().getSqlStyle(), "primaryIncrSeq");
        return StrUtil.concat(false, this.getModel().getTableName(), "_", primaryIncrSeq);
    }

    private @NotNull Element getSelectKeyElement(@NotNull F primaryField) {
        org.dom4j.Element selectKeyElement = this.createXmlElement("selectKey");
        selectKeyElement.addAttribute("keyProperty", primaryField.getName());
        selectKeyElement.addAttribute("resultType", primaryField.getJavaTypeName());
        selectKeyElement.addAttribute("order", "BEFORE");
        selectKeyElement.addText(StrUtil.concat(false, "select ",
                this.wrapEscapeCharacter(this.getPrimaryIncrSeqName()),
                ".NEXTVAL  from dual"));
        return selectKeyElement;
    }

    @Override
    public Element generateSave() {
        F primaryField = this.getModel().getPrimaryField();
        org.dom4j.Element insertElement = this.createXmlElement("insert");
        insertElement.addAttribute("id", CommonStaticField.SAVE_METHOD_NAME);
        insertElement.addAttribute("parameterType", "Map");
        List<F> baseFields = this.getModel().getBaseFields();
        if (StrUtil.equals(primaryField.getJavaTypeName(), Long.class.getName()) || StrUtil.equals(primaryField.getJavaTypeName(), Integer.class.getName())) {
            insertElement.addAttribute("keyProperty", primaryField.getName());
            insertElement.add(this.getSelectKeyElement(primaryField));
        }
        insertElement.addText(StrUtil.concat(false,
                CommonStaticField.BREAK_WRAP,
                "insert into ",
                this.wrapEscapeCharacter(this.getModel().getTableName()),
                "(",
                baseFields.stream().map(item -> this.wrapEscapeCharacter(item.getSqlStyleName())).collect(Collectors.joining(",")),
                ") values (",
                String.join(",", baseFields.stream().map(item -> wrapMybatisPrecompileStr(StrUtil.concat(false, item.getName(), ",jdbcType=", item.getMybatisJdbcType().name())))
                        .collect(Collectors.joining(","))),
                ")",
                CommonStaticField.BREAK_WRAP));
        return insertElement;
    }

    private @NotNull Element getBatchSaveForEachElement(@NotNull List<F> baseFields) {
        org.dom4j.Element batchSaveForEachElement = this.getForEachElement("list", "item", null, null, null, "UNION ALL");
        String text = StrUtil.concat(false, "select ",
                String.join(",", baseFields.stream().map(item -> wrapMybatisPrecompileStr(StrUtil.concat(false, "item.", item.getName(), ",jdbcType=", item.getMybatisJdbcType().name())))
                        .collect(Collectors.joining(","))),
                "from dual"
        );
        batchSaveForEachElement.addText(text);
        return batchSaveForEachElement;
    }

    @Override
    public Element generateBatchSave() {
        F primaryField = this.getModel().getPrimaryField();
        org.dom4j.Element batchInsertElement = this.createXmlElement("insert");
        batchInsertElement.addAttribute("id", CommonStaticField.BATCH_SAVE_METHOD_NAME);
        batchInsertElement.addAttribute("parameterType", "List");
        List<F> baseFields = this.getModel().getBaseFields();
        if (StrUtil.equals(primaryField.getJavaTypeName(), Long.class.getName()) || StrUtil.equals(primaryField.getJavaTypeName(), Integer.class.getName())) {
            batchInsertElement.addAttribute("keyProperty", primaryField.getName());
            baseFields = baseFields.stream().filter(item -> !item.equals(primaryField)).toList();
            String insertText = StrUtil.concat(false, CommonStaticField.BREAK_WRAP, "insert into ",
                    this.wrapEscapeCharacter(this.getModel().getTableName()),
                    "(",
                    this.wrapEscapeCharacter(primaryField.getSqlStyleName()),
                    ",",
                    baseFields.stream().map(item -> this.wrapEscapeCharacter(item.getSqlStyleName())).collect(Collectors.joining(",")),
                    ") select ",
                    this.wrapEscapeCharacter(this.getPrimaryIncrSeqName()), ".NEXTVAL,temp.* from (",
                    CommonStaticField.BREAK_WRAP);
            batchInsertElement.addText(insertText);
            batchInsertElement.add(this.getBatchSaveForEachElement(baseFields));
            batchInsertElement.addText(") temp");
        } else {
            String text = StrUtil.concat(false, CommonStaticField.BREAK_WRAP, "insert into ",
                    this.wrapEscapeCharacter(this.getModel().getTableName()),
                    "(",
                    baseFields.stream().map(item -> this.wrapEscapeCharacter(item.getSqlStyleName())).collect(Collectors.joining(",")),
                    ") values",
                    CommonStaticField.BREAK_WRAP);
            batchInsertElement.addText(text);
            String forEachText = StrUtil.concat(false,
                    "(",
                    String.join(",", baseFields.stream().map(item -> wrapMybatisPrecompileStr(StrUtil.concat(false, "item.", item.getName(), ",jdbcType=", item.getMybatisJdbcType().name())))
                            .collect(Collectors.joining(","))),
                    ")");
            org.dom4j.Element forEachElement = this.getForEachElement("list", null, null, null, null, null);
            forEachElement.addText(forEachText);
            batchInsertElement.add(forEachElement);
        }
        return batchInsertElement;
    }

    @Override
    public @NotNull List<String> getFieldsJoinTextList(@NotNull List<F> fields) {
        List<String> fieldsJoinText = new ArrayList<>();
        for (F field : fields) {
            if (field.isClassJoinField()) {
                T leftModel = field.getLeftModel();
                String leftJoinField = getLeftJoinField(leftModel, field);
                String leftTable = leftModel.getTableName();
                String leftTableAs = this.getJoinLeftTableAsName(field);

                T rightModel = field.getRightModel();
                String rightJoinField = getRightJoinField(rightModel, field);
                String rightTable = rightModel.getTableName();
                String rightTableAs = this.getJoinRightTableAsName(field);

                JoinType joinType = field.getJoinType();
                JoinCondition joinCondition = field.getJoinCondition();

                fieldsJoinText.add(StrUtil.concat(false, joinType.name(),
                        " join ",
                        this.wrapEscapeCharacter(leftTable),
                        " ",
                        this.wrapEscapeCharacter(leftTableAs),
                        " on ",
                        this.wrapEscapeCharacter(leftTableAs),
                        ".",
                        this.wrapEscapeCharacter(leftJoinField),
                        " = ",
                        this.wrapEscapeCharacter(rightTableAs),
                        ".",
                        this.wrapEscapeCharacter(rightJoinField)));
            }
        }
        return fieldsJoinText;
    }

    @Override
    public Element generateUpdate() {
        org.dom4j.Element updateElement = this.createXmlElement("update");
        updateElement.addAttribute("id", CommonStaticField.UPDATE_METHOD_NAME);
        updateElement.addAttribute("parameterType", "Map");
        String text = StrUtil.concat(false, CommonStaticField.BREAK_WRAP, "update ", this.wrapEscapeCharacter(this.getModel().getTableName()), CommonStaticField.BREAK_WRAP);
        updateElement.addText(text);
        org.dom4j.Element setElement = this.createXmlElement("set");
        this.getModel().getBaseFields().forEach(item -> {
            Element ifNotNullElement = this.getIfNotNullElement(StrUtil.concat(false, "new", StrUtil.upperFirst(item.getName())));
            ifNotNullElement.addText(StrUtil.concat(false,
                    wrapEscapeCharacter(item.getSqlStyleName()),
                    " = ",
                    wrapMybatisPrecompileStr(StrUtil.concat(false, "new", StrUtil.upperFirst(item.getName()), ",jdbcType=", item.getMybatisJdbcType().name())), ","));
            setElement.add(ifNotNullElement);
            Element ifSetNullElement = this.getIfSetNullElement(item.getName());
            ifSetNullElement.addText(StrUtil.concat(false, wrapEscapeCharacter(item.getSqlStyleName()), " = null,"));
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