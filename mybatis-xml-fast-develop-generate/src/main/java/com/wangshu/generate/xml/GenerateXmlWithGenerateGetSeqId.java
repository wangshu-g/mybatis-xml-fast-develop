package com.wangshu.generate.xml;

import com.wangshu.exception.MessageException;
import com.wangshu.generate.metadata.field.ColumnInfo;
import com.wangshu.generate.metadata.model.ModelInfo;
import com.wangshu.tool.CommonStaticField;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public abstract class GenerateXmlWithGenerateGetSeqId<T extends ModelInfo<?, F>, F extends ColumnInfo<?, T>> extends GenerateXml<T, F> {

    public GenerateXmlWithGenerateGetSeqId(@NotNull T model) {
        super(model);
    }

    public GenerateXmlWithGenerateGetSeqId(@NotNull T model, @Nullable Consumer<MessageException> message) {
        super(model, message);
    }

    @Override
    public void generateMapperDocument() {
        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("UTF-8");
        document.setName(this.getModel().getMapperName());
        document.addDocType("mapper", CommonStaticField.MYBATIS_XML_DOCTYPE, null);
        org.dom4j.Element rootElement = this.createXmlElement("mapper");
        rootElement.addAttribute("namespace", this.getModel().getMapperFullName());
        rootElement.addText(CommonStaticField.BREAK_WRAP);
        org.dom4j.Element generateResultMap = this.generateResultMap();
        rootElement.add(generateResultMap);
        rootElement.addText(CommonStaticField.BREAK_WRAP);
        org.dom4j.Element getSequenceIdElement = this.generateGetSequenceId();
        rootElement.add(getSequenceIdElement);
        rootElement.addText(CommonStaticField.BREAK_WRAP);
        org.dom4j.Element insertElement = this.generateSave();
        rootElement.add(insertElement);
        rootElement.addText(CommonStaticField.BREAK_WRAP);
        org.dom4j.Element batchInsertElement = this.generateBatchSave();
        rootElement.add(batchInsertElement);
        rootElement.addText(CommonStaticField.BREAK_WRAP);
        org.dom4j.Element deleteElement = this.generateDelete();
        rootElement.add(deleteElement);
        rootElement.addText(CommonStaticField.BREAK_WRAP);
        org.dom4j.Element updateElement = this.generateUpdate();
        rootElement.add(updateElement);
        rootElement.addText(CommonStaticField.BREAK_WRAP);
        org.dom4j.Element selectElement = this.generateSelect();
        rootElement.add(selectElement);
        rootElement.addText(CommonStaticField.BREAK_WRAP);
        org.dom4j.Element getListElement = this.generateGetList();
        rootElement.add(getListElement);
        rootElement.addText(CommonStaticField.BREAK_WRAP);
        org.dom4j.Element getNestListElement = this.generateGetNestList();
        rootElement.add(getNestListElement);
        rootElement.addText(CommonStaticField.BREAK_WRAP);
        org.dom4j.Element getTotalElement = this.generateGetTotal();
        rootElement.add(getTotalElement);
        document.setRootElement(rootElement);
        this.setMapperDocument(document);
    }

    abstract org.dom4j.Element generateGetSequenceId();

}
