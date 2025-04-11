package com.wangshu.generate.metadata.field;

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

import com.wangshu.annotation.Column;
import com.wangshu.annotation.Join;
import com.wangshu.enu.Condition;
import com.wangshu.enu.JoinCondition;
import com.wangshu.enu.JoinType;
import com.wangshu.generate.metadata.model.ModelInfo;

import java.util.List;

@lombok.Data
public abstract class AbstractColumnInfo<T, M extends ModelInfo<?, ?>> implements ColumnInfo<T, M> {

    private T metaData;
    private M model;
    private String name;
    private String sqlStyleName;
    private String javaTypeName;

    private Column column;
    private String title;
    private String comment;
    private String jdbcType;
    private List<Condition> conditions;
    private boolean baseField;
    private boolean primaryField;
    private boolean keywordField;

    private Join join;
    private M leftModel;
    private M rightModel;
    private boolean joinField;
    private boolean classJoinField;
    private boolean collectionJoinField;
    private List<String> leftSelectFieldNames;
    private String leftJoinField;
    private String rightJoinField;
    private String indirectJoinField;
    private JoinType joinType;
    private JoinCondition joinCondition;
    private String infix;

    public AbstractColumnInfo() {

    }

    public AbstractColumnInfo(T metaData, M model) {
        this.metaData = metaData;
        this.model = model;
        this.initColumnInfo(metaData, model);
        this.initJoinInfo(metaData, model);
    }

    public void initColumnInfo(T metaData, M model) {
        this.setTitle(ColumnInfo.super.getTitle());
        this.setComment(ColumnInfo.super.getComment());
        this.setJdbcType(null);
        this.setConditions(ColumnInfo.super.getConditions());
        this.setBaseField(ColumnInfo.super.isBaseField());
        this.setPrimaryField(ColumnInfo.super.isPrimaryField());
        this.setKeywordField(ColumnInfo.super.isKeywordField());
    }

    public void initJoinInfo(T metaData, M model) {
        this.setJoinField(ColumnInfo.super.isJoinField());
        this.setClassJoinField(ColumnInfo.super.isClassJoinField());
        this.setCollectionJoinField(ColumnInfo.super.isCollectionJoinField());
        this.setLeftSelectFieldNames(ColumnInfo.super.getLeftSelectFieldNames());
        this.setLeftJoinField(ColumnInfo.super.getLeftJoinField());
        this.setRightJoinField(ColumnInfo.super.getRightJoinField());
        this.setIndirectJoinField(ColumnInfo.super.getIndirectJoinField());
        this.setJoinType(ColumnInfo.super.getJoinType());
        this.setJoinCondition(ColumnInfo.super.getJoinCondition());
        this.setInfix(ColumnInfo.super.getInfix());
    }

}
