package com.wangshu.generate.xml;

import com.wangshu.exception.MessageException;
import com.wangshu.generate.metadata.field.ColumnInfo;
import com.wangshu.generate.metadata.model.ModelInfo;

import java.util.function.Consumer;

public class GenerateXmlMysql<T extends ModelInfo<?, F>, F extends ColumnInfo<?, T>> extends GenerateXml<T, F> {

    public GenerateXmlMysql(T model) {
        super(model);
    }

    public GenerateXmlMysql(T model, Consumer<MessageException> message) {
        super(model, message);
    }

}