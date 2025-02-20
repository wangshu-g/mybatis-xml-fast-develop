package com.wangshu.generate.xml;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.wangshu.enu.Condition;
import com.wangshu.enu.JoinCondition;
import com.wangshu.enu.JoinType;
import com.wangshu.exception.MessageException;
import com.wangshu.generate.GenerateInfo;
import com.wangshu.generate.metadata.field.ColumnInfo;
import com.wangshu.generate.metadata.model.ModelInfo;
import com.wangshu.tool.CommonStaticField;
import org.apache.ibatis.type.JdbcType;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@lombok.Data
public abstract class GenerateXml<T extends ModelInfo<?, F>, F extends ColumnInfo<?, T>> implements GenerateInfo {

    private T model;
    private Document mapperDocument;
    private Consumer<MessageException> message;

    public GenerateXml(@NotNull T model) {
        this.model = model;
        this.message = null;
    }

    public GenerateXml(@NotNull T model, Consumer<MessageException> message) {
        this.model = model;
        this.message = message;
    }

    public boolean writeXml() {
        return writeXml(this.getModel().getGenerateXmlFilePath());
    }

    public boolean writeXml(String path) {
        if (Objects.isNull(this.getMapperDocument())) {
            this.generateMapperDocument();
        }
        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setTrimText(false);
            format.setSuppressDeclaration(true);
            format.setEncoding(this.getMapperDocument().getXMLEncoding());
            File file = FileUtil.touch(path);
            file.deleteOnExit();
            XMLWriter writer = new XMLWriter(new FileWriter(file), format);
            writer.write(this.getMapperDocument());
            writer.flush();
            writer.close();
            return true;
        } catch (Exception e) {
            this.printWarn("导出xml出现异常", e);
        }
        return false;
    }

    public Document generateMapperDocument() {
        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("UTF-8");
        document.setName(this.getModel().getMapperName());
        document.addDocType("mapper", CommonStaticField.MYBATIS_XML_DOCTYPE, null);
        org.dom4j.Element rootElement = this.createXmlElement("mapper");
        rootElement.addAttribute("namespace", this.model.getMapperFullName());
        rootElement.addText(CommonStaticField.WRAP);
        org.dom4j.Element generateResultMap = this.generateResultMap();
        rootElement.add(generateResultMap);
        rootElement.addText(CommonStaticField.WRAP);
        org.dom4j.Element insertElement = this.generateSave();
        rootElement.add(insertElement);
        rootElement.addText(CommonStaticField.WRAP);
        org.dom4j.Element batchInsertElement = this.generateBatchSave();
        rootElement.add(batchInsertElement);
        rootElement.addText(CommonStaticField.WRAP);
        org.dom4j.Element deleteElement = this.generateDelete();
        rootElement.add(deleteElement);
        rootElement.addText(CommonStaticField.WRAP);
        org.dom4j.Element updateElement = this.generateUpdate();
        rootElement.add(updateElement);
        rootElement.addText(CommonStaticField.WRAP);
        org.dom4j.Element selectElement = this.generateSelect();
        rootElement.add(selectElement);
        rootElement.addText(CommonStaticField.WRAP);
        org.dom4j.Element getListElement = this.generateGetList();
        rootElement.add(getListElement);
        rootElement.addText(CommonStaticField.WRAP);
        org.dom4j.Element getNestListElement = this.generateGetNestList();
        rootElement.add(getNestListElement);
        rootElement.addText(CommonStaticField.WRAP);
        org.dom4j.Element getTotalElement = this.generateGetTotal();
        rootElement.add(getTotalElement);
        document.setRootElement(rootElement);
        this.setMapperDocument(document);
        return document;
    }

    public org.dom4j.Element createXmlElement(String elementName) {
        return DocumentHelper.createElement(elementName);
    }

    public org.dom4j.Element generateResultMap() {
        org.dom4j.Element resultMapElement = this.createXmlElement("resultMap");
        resultMapElement.addAttribute("id", this.getModel().getModelName());
        resultMapElement.addAttribute("type", this.getModel().getModelFullName());

        List<F> tempFields = new ArrayList<>();
        tempFields.addAll(this.getModel().getBaseFields());
        tempFields.addAll(this.getModel().getClazzJoinFields());
        tempFields.addAll(this.getModel().getCollectionJoinFields());

        for (F field : tempFields) {
            resultMapElement.add(this.getFieldResultMap(field));
        }

        return resultMapElement;
    }

    public org.dom4j.Element generateSave() {
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
        insertElement.addText(StrUtil.concat(false, CommonStaticField.WRAP, "insert into ", this.getBackQuoteStr(this.getModel().getTableName()), "(", baseFields.stream().map(item -> getBackQuoteStr(item.getName())).collect(Collectors.joining(",")), ") values (", String.join(",", baseFields.stream().map(item -> getPreCompileStr(item.getName())).collect(Collectors.joining(","))), ");", CommonStaticField.WRAP));
        return insertElement;
    }

    public org.dom4j.Element generateBatchSave() {
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
        String text = StrUtil.concat(false, CommonStaticField.WRAP, "insert into ", this.getBackQuoteStr(this.getModel().getTableName()), "(", baseFields.stream().map(item -> getBackQuoteStr(item.getName())).collect(Collectors.joining(",")), ") values", CommonStaticField.WRAP);
        batchInsertElement.addText(text);
        String forEachText = StrUtil.concat(false, "(", String.join(",", baseFields.stream().map(item -> getPreCompileStr(StrUtil.concat(false, "item.", item.getName()))).collect(Collectors.joining(","))), ")");
        org.dom4j.Element forEachElement = this.getForEachElement("list", null, null, null, null, null);
        forEachElement.addText(forEachText);
        batchInsertElement.add(forEachElement);
        return batchInsertElement;
    }

    public org.dom4j.Element generateDelete() {
        org.dom4j.Element deleteElement = this.createXmlElement("delete");
        deleteElement.addAttribute("id", CommonStaticField.DELETE_METHOD_NAME);
        deleteElement.addAttribute("parameterType", "Map");
        deleteElement.addText(StrUtil.concat(false, CommonStaticField.WRAP, "delete from ", this.getBackQuoteStr(this.getModel().getTableName())));
        org.dom4j.Element whereElement = this.createXmlElement("where");

        List<F> tempFields = new ArrayList<>(this.getModel().getBaseFields());
        this.getIf(tempFields, whereElement::add);

        deleteElement.add(whereElement);
        return deleteElement;
    }

    public org.dom4j.Element generateUpdate() {
        org.dom4j.Element updateElement = this.createXmlElement("update");
        updateElement.addAttribute("id", CommonStaticField.UPDATE_METHOD_NAME);
        updateElement.addAttribute("parameterType", "Map");
        String text = StrUtil.concat(false, CommonStaticField.WRAP, "update ", this.getBackQuoteStr(this.getModel().getTableName()), CommonStaticField.WRAP);
        updateElement.addText(text);
        org.dom4j.Element setElement = this.createXmlElement("set");
        this.getModel().getBaseFields().forEach(item -> {
            Element ifNotNullElement = this.getIfNotNullElement(StrUtil.concat(false, "new", StrUtil.upperFirst(item.getName())));
            ifNotNullElement.addText(StrUtil.concat(false, getBackQuoteStr(item.getName()), " = ", getPreCompileStr(StrUtil.concat(false, "new", StrUtil.upperFirst(item.getName()))), ","));
            setElement.add(ifNotNullElement);
            Element ifSetNullElement = this.getIfSetNullElement(item.getName());
            ifSetNullElement.addText(StrUtil.concat(false, getBackQuoteStr(item.getName()), " = null,"));
            setElement.add(ifSetNullElement);
        });
        updateElement.add(setElement);
        org.dom4j.Element whereElement = this.createXmlElement("where");

        List<F> tempFields = new ArrayList<>(this.getModel().getBaseFields());
        this.getIf(tempFields, whereElement::add);

        updateElement.add(whereElement);
        return updateElement;
    }

    public org.dom4j.Element generateSelect() {
        org.dom4j.Element selectElement = this.createXmlElement("select");
        selectElement.addAttribute("id", CommonStaticField.SELECT_METHOD_NAME);
        selectElement.addAttribute("parameterType", "Map");
        selectElement.addAttribute("resultType", this.getModel().getModelFullName());
        selectElement.addAttribute("resultMap", this.getModel().getModelName());
        selectElement.addText(StrUtil.concat(false, CommonStaticField.WRAP, "select ", "\n"));

        List<F> tempFields = new ArrayList<>();
        tempFields.addAll(this.getModel().getBaseFields());
        tempFields.addAll(this.getModel().getClazzJoinFields());
        tempFields.addAll(this.getModel().getCollectionJoinFields());

        selectElement.addText(this.getFieldsSelectText(tempFields));
        selectElement.addText(CommonStaticField.WRAP);

        selectElement.addText(StrUtil.concat(false, "from ", this.getBackQuoteStr(this.getModel().getTableName())));
        selectElement.addText(CommonStaticField.WRAP);

        List<F> joinFields = new ArrayList<>();
        joinFields.addAll(this.getModel().getClazzJoinFields());
        joinFields.addAll(this.getModel().getCollectionJoinFields());

        selectElement.addText(this.getFieldsJoinText(joinFields));

        org.dom4j.Element whereElement = this.createXmlElement("where");
        this.getIf(tempFields, whereElement::add);

        Element ifForUpdateNotNullElement = this.getIfNotNullElement("enableForUpdate");
        ifForUpdateNotNullElement.addText("for update");
        whereElement.add(ifForUpdateNotNullElement);

        selectElement.add(whereElement);
        return selectElement;
    }

    public org.dom4j.Element generateGetList() {
        org.dom4j.Element listElement = this.createXmlElement("select");
        listElement.addAttribute("id", CommonStaticField.GET_LIST_METHOD_NAME);
        listElement.addAttribute("parameterType", "Map");
        listElement.addAttribute("resultType", "Map");
        listElement.addText(CommonStaticField.WRAP);
        listElement.addText("select ");
        listElement.addText(CommonStaticField.WRAP);

//        List<F> collectionFields = this.getModel().getCollectionFields().stream().filter(item -> {
//            T leftModel = item.getLeftModel();
//            return !StrUtil.equals(leftModel.getModelFullName(), this.getModel().getModelFullName());
//        }).toList();

        List<F> tempFields = new ArrayList<>();
        tempFields.addAll(this.getModel().getBaseFields());
        tempFields.addAll(this.getModel().getClazzJoinFields());
//        tempFields.addAll(collectionFields);

        listElement.addText(this.getFieldsSelectText(tempFields));
        listElement.addText(CommonStaticField.WRAP);

        listElement.addText(StrUtil.concat(false, "from ", this.getBackQuoteStr(this.getModel().getTableName()), CommonStaticField.WRAP));
        List<F> joinFields = new ArrayList<>();
        joinFields.addAll(this.getModel().getClazzJoinFields());
//        joinFields.addAll(collectionFields);

        listElement.addText(this.getFieldsJoinText(joinFields));

        org.dom4j.Element whereElement = this.createXmlElement("where");

        this.getIf(tempFields, whereElement::add);

        listElement.add(whereElement);
        listElement.addText(CommonStaticField.WRAP);
        listElement.add(this.getOrder());
        listElement.addText(CommonStaticField.WRAP);
        listElement.add(this.getLimit());
        return listElement;
    }

    public org.dom4j.Element generateGetNestList() {
        org.dom4j.Element nestListElement = this.createXmlElement("select");
        nestListElement.addAttribute("id", CommonStaticField.GET_NEST_LIST_METHOD_NAME);
        nestListElement.addAttribute("parameterType", "Map");
        nestListElement.addAttribute("resultType", this.getModel().getModelFullName());
        nestListElement.addAttribute("resultMap", this.getModel().getModelName());
        nestListElement.addText(CommonStaticField.WRAP);
        nestListElement.addText("select ");
        nestListElement.addText(CommonStaticField.WRAP);

        List<F> tempFields = new ArrayList<>();
        tempFields.addAll(this.getModel().getBaseFields());
        tempFields.addAll(this.getModel().getClazzJoinFields());
        tempFields.addAll(this.getModel().getCollectionJoinFields());

        nestListElement.addText(this.getFieldsSelectText(tempFields));
        nestListElement.addText(CommonStaticField.WRAP);

        nestListElement.addText(StrUtil.concat(false, "from ", this.getBackQuoteStr(this.getModel().getTableName())));
        nestListElement.addText(CommonStaticField.WRAP);

        List<F> joinFields = new ArrayList<>();
        joinFields.addAll(this.getModel().getClazzJoinFields());
        joinFields.addAll(this.getModel().getCollectionJoinFields());

        nestListElement.addText(this.getFieldsJoinText(joinFields));

        org.dom4j.Element whereElement = this.createXmlElement("where");

        this.getIf(tempFields, whereElement::add);

        nestListElement.add(whereElement);
        nestListElement.addText(CommonStaticField.WRAP);
        nestListElement.add(this.getOrder());
        nestListElement.addText(CommonStaticField.WRAP);
        nestListElement.add(this.getLimit());
        return nestListElement;
    }

    public org.dom4j.Element generateGetTotal() {
        org.dom4j.Element totalElement = this.createXmlElement("select");
        totalElement.addAttribute("id", CommonStaticField.GET_TOTAL_METHOD_NAME);
        totalElement.addAttribute("parameterType", "Map");
        totalElement.addAttribute("resultType", Integer.class.getSimpleName());
        totalElement.addText(CommonStaticField.WRAP);
        totalElement.addText(StrUtil.concat(false, "select count(", this.getBackQuoteStr(this.getModel().getTableName()), ".", this.getBackQuoteStr(this.getModel().getPrimaryField().getName()), ") from ", this.getBackQuoteStr(this.getModel().getTableName())));
        totalElement.addText(CommonStaticField.WRAP);

        List<F> joinFields = new ArrayList<>();
        joinFields.addAll(this.getModel().getClazzJoinFields());
        joinFields.addAll(this.getModel().getCollectionJoinFields());

        totalElement.addText(this.getFieldsJoinText(joinFields));

        List<F> tempFields = new ArrayList<>();
        tempFields.addAll(this.getModel().getBaseFields());
        tempFields.addAll(this.getModel().getClazzJoinFields());
        tempFields.addAll(this.getModel().getCollectionJoinFields());

        org.dom4j.Element whereElement = this.createXmlElement("where");

        this.getIf(tempFields, whereElement::add);

        totalElement.add(whereElement);

        return totalElement;
    }

    public @NotNull org.dom4j.Element getFieldResultMap(@NotNull F field) {
        if (field.isBaseField()) {
            return this.getResultMapElement(field);
        } else {
            T leftModel = field.getLeftModel();
            List<F> baseFields = leftModel.getBaseFields();
            List<String> leftSelectFieldNames = field.getLeftSelectFieldNames();
            if (!StrUtil.equals(leftSelectFieldNames.getFirst(), "*")) {
                baseFields = baseFields.stream().filter(item -> leftSelectFieldNames.contains(item.getName())).toList();
            }
            org.dom4j.Element element = null;
            if (field.isCollectionJoinField()) {
                element = this.getCollectionElement(field.getName(), leftModel.getModelFullName());
                element.addAttribute("javaType", List.class.getTypeName());
            } else {
                element = this.getAssociationElement(field.getName(), leftModel.getModelFullName());
            }
            for (F baseField : baseFields) {
                String property = baseField.getName();
                String column = StrUtil.concat(false, field.getName(), field.getInfix(), StringUtils.capitalize(property));
                JdbcType mybatisJdbcType = baseField.getMybatisJdbcType();
                Element idResultElement = this.getResultMapElement(column, mybatisJdbcType.name(), property, baseField.isPrimaryField());
                element.add(idResultElement);
            }
            return element;
        }
    }

    public String getFieldsSelectText(@NotNull List<F> fields) {
        List<String> fieldsSelectTextList = this.getFieldsSelectTextList(fields);
        return String.join(",\n", fieldsSelectTextList);
    }

    public @NotNull List<String> getFieldsSelectTextList(@NotNull List<F> fields) {
        List<String> fieldsSelectText = new ArrayList<>();
        for (F field : fields) {
            if (field.isBaseField()) {
                String table = this.getModel().getTableName();
                String columnName = field.getName();
                fieldsSelectText.add(this.getSelectText(table, columnName));
            } else if (field.isClassJoinField() || field.isCollectionJoinField()) {
                T leftModel = field.getLeftModel();
                List<F> baseFields = leftModel.getBaseFields();
                List<String> leftSelectFields = field.getLeftSelectFieldNames();
                if (!StrUtil.equals(leftSelectFields.getFirst(), "*")) {
                    baseFields = baseFields.stream().filter(item -> leftSelectFields.contains(item.getName())).toList();
                }
//                baseFields = baseFields.stream().filter(item -> !item.isPrimaryField()).toList();
                for (F baseField : baseFields) {
                    String table = getJoinLeftTableAsName(field);
                    String columnName = baseField.getName();
                    String columnAsName = StrUtil.concat(false, field.getName(), field.getInfix(), StringUtils.capitalize(columnName));
                    fieldsSelectText.add(this.getSelectText(table, columnName, columnAsName));
                }
            }
        }
        return fieldsSelectText;
    }

    public String getJoinLeftTableAsName(@NotNull F joinField) {
        T leftModel = joinField.getLeftModel();
        T rightModel = joinField.getRightModel();
        return joinField.getName();
    }

    public String getJoinRightTableAsName(@NotNull F joinField) {
        T leftModel = joinField.getLeftModel();
        T rightModel = joinField.getRightModel();
        if (StrUtil.isBlank(joinField.getIndirectJoinField()) && StrUtil.equals(rightModel.getModelName(), this.getModel().getModelName())) {
            return rightModel.getTableName();
        } else {
//            存在间接关联
            List<F> list = this.getModel().getClazzJoinFields().stream().filter(item -> StrUtil.equals(item.getLeftModel().getModelName(), rightModel.getModelName())).toList();
            if (list.isEmpty()) {
                list = this.getModel().getCollectionJoinFields().stream().filter(item -> StrUtil.equals(item.getLeftModel().getModelName(), item.getLeftModel().getModelFullName())).toList();
            }
            if (StrUtil.isBlank(joinField.getIndirectJoinField())) {
                return this.getJoinLeftTableAsName(list.getFirst());
            } else {
                return this.getJoinLeftTableAsName(list.stream().collect(Collectors.toMap(k -> k.getName(), v -> v)).get(joinField.getIndirectJoinField()));
            }
        }
    }

    public String getFieldsJoinText(@NotNull List<F> fields) {
        return String.join("\n", this.getFieldsJoinTextList(fields));
    }

    public @NotNull List<String> getFieldsJoinTextList(@NotNull List<F> fields) {
        List<String> fieldsJoinText = new ArrayList<>();
        for (F field : fields) {
            if (field.isClassJoinField() || field.isCollectionJoinField()) {
                T leftModel = field.getLeftModel();
                String leftJoinField = field.getLeftJoinField();
                String leftTable = leftModel.getTableName();
                String leftTableAs = this.getJoinLeftTableAsName(field);

                T rightModel = field.getRightModel();
                String rightJoinField = field.getRightJoinField();
                if (Objects.isNull(rightModel)) {
                    throw new RuntimeException("error");
                }
                String rightTable = rightModel.getTableName();
                String rightTableAs = this.getJoinRightTableAsName(field);

                JoinType joinType = field.getJoinType();
                JoinCondition joinCondition = field.getJoinCondition();

                switch (joinCondition) {
                    case equal -> {
                        fieldsJoinText.add(StrUtil.concat(false, joinType.name(), " join ", this.getBackQuoteStr(leftTable), " as ", this.getBackQuoteStr(leftTableAs), " on ", this.getBackQuoteStr(leftTableAs), ".", this.getBackQuoteStr(leftJoinField), " = ", this.getBackQuoteStr(rightTableAs), ".", this.getBackQuoteStr(rightJoinField)));
                    }
                    case great -> {
                        fieldsJoinText.add(StrUtil.concat(false, joinType.name(), " join ", this.getBackQuoteStr(leftTable), " as ", this.getBackQuoteStr(leftTableAs), " on ", this.getBackQuoteStr(leftTableAs), ".", this.getBackQuoteStr(leftJoinField), " > ", this.getBackQuoteStr(rightTableAs), ".", this.getBackQuoteStr(rightJoinField)));
                    }
                    case less -> {
                        fieldsJoinText.add(StrUtil.concat(false, joinType.name(), " join ", this.getBackQuoteStr(leftTable), " as ", this.getBackQuoteStr(leftTableAs), " on ", this.getBackQuoteStr(leftTableAs), ".", this.getBackQuoteStr(leftJoinField), " < ", this.getBackQuoteStr(rightTableAs), ".", this.getBackQuoteStr(rightJoinField)));
                    }
                    case like -> {
                        fieldsJoinText.add(StrUtil.concat(false, joinType.name(), " join ", this.getBackQuoteStr(leftTable), " as ", this.getBackQuoteStr(leftTableAs), " on instr(", this.getBackQuoteStr(leftTableAs), ".", this.getBackQuoteStr(leftJoinField), ",", this.getBackQuoteStr(rightTableAs), ".", this.getBackQuoteStr(rightJoinField), ")"));
                    }
                }
            }
        }
        return fieldsJoinText;
    }

    public void getIf(@NotNull List<F> fields, @NotNull Consumer<org.dom4j.Element> elementConsumer) {
        List<Element> orElements = new ArrayList<>();
        for (F field : fields) {
            if (field.isBaseField()) {
                List<Condition> conditions = field.getConditions();
                if (conditions.getFirst().equals(Condition.all)) {
                    conditions = Condition.getEntries();
                }
                conditions.forEach((condition -> {
                    if (!condition.equals(Condition.all)) {
                        if (condition.name().contains("or")) {
                            orElements.add(this.getBaseFieldIfElement(field, condition));
                        } else {
                            elementConsumer.accept(this.getBaseFieldIfElement(field, condition));
                        }
                    }
                }));
            } else if (field.isClassJoinField() || field.isCollectionJoinField()) {
                T leftModel = field.getLeftModel();
                List<F> baseFields = leftModel.getBaseFields();
                List<String> leftSelectFieldNames = field.getLeftSelectFieldNames();
                if (!StrUtil.equals(leftSelectFieldNames.getFirst(), "*")) {
                    baseFields = baseFields.stream().filter(item -> leftSelectFieldNames.contains(item.getName())).toList();
                }
                baseFields.forEach(item -> {
                    List<Condition> conditions = item.getConditions();
                    if (Objects.nonNull(conditions)) {
                        if (conditions.getFirst().equals(Condition.all)) {
                            conditions = Condition.getEntries();
                        }
                        conditions.forEach((condition -> {
                            if (!condition.equals(Condition.all)) {
                                if (condition.name().contains("or")) {
                                    orElements.add(this.getJoinFieldIfElement(field, leftModel, item, condition));
                                } else {
                                    elementConsumer.accept(this.getJoinFieldIfElement(field, leftModel, item, condition));
                                }
                            }
                        }));
                    }
                });
            }
        }
        Element enableOrElement = DocumentHelper.createElement("if").addAttribute("test", "enableOr != null").addText("and (0 = 1");
        orElements.forEach(enableOrElement::add);
        enableOrElement.addText(")");
        elementConsumer.accept(enableOrElement);
    }

    public org.dom4j.Element getBaseFieldIfElement(@NotNull F field, @NotNull Condition condition) {
        String testConditionName = StrUtil.concat(false, field.getName(), StringUtils.capitalize(condition.equals(Condition.equal) ? "" : condition.name()));
        org.dom4j.Element ifElement = this.getIfNotNullElement(testConditionName);
        this.getBaseFieldIfText(ifElement, field, condition, testConditionName);
        return ifElement;
    }

    public void getBaseFieldIfText(org.dom4j.Element ifElement, @NotNull F field, @NotNull Condition condition, String testConditionName) {
        boolean isAndStr = condition.name().indexOf("or") != 0;
        this.getIfText(ifElement, isAndStr, this.getModel().getTableName(), field.getName(), condition, testConditionName);
    }

    /**
     * @param joinField   当前model连接其他类的属性
     * @param leftModel   field对应join的类
     * @param selectField leftModel类的基本属性
     * @param condition   本次条件
     * @return org.dom4j.Element ifElement
     **/
    public org.dom4j.Element getJoinFieldIfElement(@NotNull F joinField, T leftModel, @NotNull F selectField, @NotNull Condition condition) {
        String testConditionName = StrUtil.concat(false, joinField.getName(), joinField.getInfix(), StringUtils.capitalize(selectField.getName()), StringUtils.capitalize(condition.equals(Condition.equal) ? "" : condition.name()));
        org.dom4j.Element ifElement = this.getIfNotNullElement(testConditionName);
        this.getJoinFieldIfText(ifElement, joinField, leftModel, selectField, condition, testConditionName);
        return ifElement;
    }

    public void getJoinFieldIfText(org.dom4j.Element ifElement, F joinField, T leftModel, @NotNull F selectField, @NotNull Condition condition, String testConditionName) {
        boolean isAndStr = condition.name().indexOf("or") != 0;
        String table = this.getJoinLeftTableAsName(joinField);
        String columnName = selectField.getName();
        this.getIfText(ifElement, isAndStr, table, columnName, condition, testConditionName);
    }

    public void getIfText(org.dom4j.Element ifElement, boolean isAndStr, String table, String columnName, Condition condition, String testConditionName) {
        String ifText = null;
        if (List.of(Condition.equal, Condition.orEqual, Condition.less, Condition.orLess, Condition.great, Condition.orGreat).contains(condition)) {
            ifText = this.getNormalIfText(table, columnName, this.getConditionStr(condition), testConditionName, isAndStr);
        } else if (List.of(Condition.like, Condition.orLike).contains(condition)) {
            ifText = this.getLikeIfText(table, columnName, testConditionName, isAndStr);
        } else if (List.of(Condition.in, Condition.orIn).contains(condition)) {
            String orAndStr = condition.name().indexOf("or") == 0 ? "or " : "and ";
            String itemText = StrUtil.concat(false, testConditionName, "Item");
            org.dom4j.Element forEachElement = this.getForEachElement(testConditionName, itemText, null, "(", ")", null);
            forEachElement.addText(CommonStaticField.WRAP);
            forEachElement.addText(this.getPreCompileStr(itemText));
            forEachElement.addText(CommonStaticField.WRAP);
            ifElement.addText(StrUtil.concat(false, CommonStaticField.WRAP, orAndStr, this.getBackQuoteStr(table), ".", this.getBackQuoteStr(columnName), " in", CommonStaticField.WRAP));
            ifElement.add(forEachElement);
            return;
        } else if (List.of(Condition.isNull, Condition.orIsNull, Condition.isNotNull, Condition.orIsNotNull).contains(condition)) {
            boolean isNull = List.of(Condition.isNull, Condition.orIsNull).contains(condition);
            ifText = this.getNullIfText(table, columnName, isAndStr, isNull);
        }
        ifElement.addText(ifText);
    }

    public org.dom4j.Element getLimit() {
        org.dom4j.Element ifElement = this.createXmlElement("if");
        ifElement.addAttribute("test", "pageIndex != null and pageSize != null");
        ifElement.addText(StrUtil.concat(false, "limit ", this.getPreCompileStr("pageIndex"), ",", this.getPreCompileStr("pageSize")));
        return ifElement;
    }

    public org.dom4j.Element getOrder() {
        org.dom4j.Element ifElement = this.createXmlElement("if");
        ifElement.addAttribute("test", "orderColumn != null");
        ifElement.addText(StrUtil.concat(false, "order by ", this.getBackQuoteStr(this.getConcatStr("orderColumn")), " ", this.getConcatStr("order")));
        return ifElement;
    }

    public org.dom4j.Element getResultMapElement(@NotNull F field) {
        return this.getResultMapElement(field.getName(), field.getMybatisJdbcType().name(), field.getName(), field.isPrimaryField());
    }

    public org.dom4j.Element getResultMapElement(String column, @NotNull String mybatisJdbcType, String property, boolean isPrimary) {
        org.dom4j.Element resultElement = isPrimary ? this.createXmlElement("id") : this.createXmlElement("result");
        resultElement.addAttribute("column", column);
        resultElement.addAttribute("jdbcType", mybatisJdbcType);
        resultElement.addAttribute("property", property);
        return resultElement;
    }

    public org.dom4j.Element getCollectionElement(String property, String ofType) {
        org.dom4j.Element collectionElement = this.createXmlElement("collection");
        collectionElement.addAttribute("property", property);
        collectionElement.addAttribute("ofType", ofType);
        return collectionElement;
    }

    public org.dom4j.Element getAssociationElement(String property, String javaType) {
        org.dom4j.Element collectionElement = this.createXmlElement("association");
        collectionElement.addAttribute("property", property);
        collectionElement.addAttribute("javaType", javaType);
        return collectionElement;
    }

    public String getSelectText(String tableName, String columnName) {
        return StrUtil.concat(false, this.getBackQuoteStr(tableName), ".", this.getBackQuoteStr(columnName));
    }

    public String getSelectText(String tableName, String columnName, String columnAsName) {
        return StrUtil.concat(false, this.getBackQuoteStr(tableName), ".", this.getBackQuoteStr(columnName), " as ", this.getBackQuoteStr(columnAsName));
    }

    public org.dom4j.Element getIfNotNullElement(String testName) {
        org.dom4j.Element ifElement = this.createXmlElement("if");
        ifElement.addAttribute("test", StrUtil.concat(false, testName, " != null"));
        return ifElement;
    }

    public org.dom4j.Element getIfSetNullElement(String testName) {
        org.dom4j.Element ifElement = this.createXmlElement("if");
        ifElement.addAttribute("test", StrUtil.concat(false, "set", StrUtil.upperFirst(testName), "Null", " != null"));
        return ifElement;
    }

    public String getNormalIfText(String tableName, String columnName, String conditionSeparator, String testConditionName, boolean isAndStr) {
        String orAndStr = isAndStr ? "and " : "or ";
        return StrUtil.concat(false, orAndStr, this.getBackQuoteStr(tableName), ".", this.getBackQuoteStr(columnName), conditionSeparator, this.getPreCompileStr(testConditionName));
    }

    public String getLikeIfText(String tableName, String columnName, String testConditionName, boolean isAndStr) {
        String orAndStr = isAndStr ? "and " : "or ";
        return StrUtil.concat(false, orAndStr, "instr(", this.getBackQuoteStr(tableName), ".", this.getBackQuoteStr(columnName), ",", this.getPreCompileStr(testConditionName), ") > 0");
    }

    public String getNullIfText(String tableName, String columnName, boolean isAndStr, boolean isNull) {
        String orAndStr = isAndStr ? "and " : "or ";
        String endStr = isNull ? " is null" : " is not null";
        return StrUtil.concat(false, orAndStr, this.getBackQuoteStr(tableName), ".", this.getBackQuoteStr(columnName), endStr);
    }

    public org.dom4j.Element getForEachElement(String collection, String item, String index, String open, String close, String separator) {
        if (StrUtil.isBlank(item)) {
            item = "item";
        }
        if (StrUtil.isBlank(index)) {
            index = "index";
        }
        if (StrUtil.isBlank(separator)) {
            separator = ",";
        }
        org.dom4j.Element forEachElement = this.createXmlElement("foreach");
        forEachElement.addAttribute("collection", collection);
        forEachElement.addAttribute("item", item);
        forEachElement.addAttribute("index", index);
        if (StrUtil.isNotBlank(open)) {
            forEachElement.addAttribute("open", open);
        }
        if (StrUtil.isNotBlank(close)) {
            forEachElement.addAttribute("close", close);
        }
        forEachElement.addAttribute("separator", separator);
        return forEachElement;
    }

    public String getConditionStr(@NotNull Condition condition) {
        if (condition.equals(Condition.equal) || condition.equals(Condition.orEqual)) {
            return " = ";
        } else if (condition.equals(Condition.less) || condition.equals(Condition.orLess)) {
            return " < ";
        } else if (condition.equals(Condition.great) || condition.equals(Condition.orGreat)) {
            return " > ";
        }
        return null;
    }

    public String getBackQuoteStr(String str) {
        return StrUtil.concat(false, "`", str, "`");
    }

    public String getPreCompileStr(String str) {
        return StrUtil.concat(false, "#{", str, "}");
    }

    public String getConcatStr(String str) {
        return StrUtil.concat(false, "${", str, "}");
    }
}
