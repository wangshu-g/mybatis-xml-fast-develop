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
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class GenerateXmlMariadb<T extends ModelInfo<?, F>, F extends ColumnInfo<?, T>> extends GenerateXml<T, F> {

    public GenerateXmlMariadb(T model) {
        super(model);
    }

    public GenerateXmlMariadb(T model, @Nullable Consumer<MessageException> messageExceptionConsumer) {
        super(model, messageExceptionConsumer);
    }

    public org.dom4j.Element generateSave() {
        F primaryField = this.getModel().getPrimaryField();
        org.dom4j.Element insertElement = this.createXmlElement("insert");
        insertElement.addAttribute("id", CommonStaticField.SAVE_METHOD_NAME);
        insertElement.addAttribute("parameterType", "Map");
        List<F> baseFields = this.getModel().getBaseFields();
        if (primaryField.isIncrPrimary()) {
            insertElement.addAttribute("useGeneratedKeys", "true");
            insertElement.addAttribute("keyProperty", primaryField.getName());
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

    public org.dom4j.Element generateBatchSave() {
        F primaryField = this.getModel().getPrimaryField();
        org.dom4j.Element batchInsertElement = this.createXmlElement("insert");
        batchInsertElement.addAttribute("id", CommonStaticField.BATCH_SAVE_METHOD_NAME);
        batchInsertElement.addAttribute("parameterType", "List");
        List<F> baseFields = this.getModel().getBaseFields();
        if (primaryField.isIncrPrimary()) {
            batchInsertElement.addAttribute("useGeneratedKeys", "true");
            batchInsertElement.addAttribute("keyProperty", primaryField.getName());
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