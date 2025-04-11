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

import cn.hutool.core.util.StrUtil;
import com.wangshu.annotation.Column;
import com.wangshu.annotation.Join;
import com.wangshu.base.model.BaseModel;
import com.wangshu.enu.SqlStyle;
import com.wangshu.generate.metadata.model.ModelElementInfo;
import com.wangshu.tool.MssqlTypeMapInfo;
import com.wangshu.tool.MysqlTypeMapInfo;
import com.wangshu.tool.PostgresqlTypeMapInfo;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@lombok.Data
public class ColumnElementInfo extends AbstractColumnInfo<VariableElement, ModelElementInfo> {

    public ColumnElementInfo(VariableElement metaData, ModelElementInfo model) {
        this.setMetaData(metaData);
        this.setModel(model);
        this.initBaseInfo(metaData, model);
        this.initColumnInfo(metaData, model);
        this.initJoinInfo(metaData, model);
    }

    public void initBaseInfo(@NotNull VariableElement metaData, ModelElementInfo model) {
        this.setName(metaData.getSimpleName().toString());
        this.setSqlStyleName(this.initSqlStyleName(metaData, model));
        this.setJavaTypeName(metaData.asType().toString());
    }

    @Override
    public void initColumnInfo(@NotNull VariableElement metaData, ModelElementInfo model) {
        Column column = metaData.getAnnotation(Column.class);
        if (Objects.nonNull(column)) {
            this.setColumn(column);
            this.setJdbcType(this.initJdbcType(metaData, column));
            this.setTitle(column.title());
            this.setComment(column.comment());
            this.setConditions(Arrays.asList(column.conditions()));
            this.setBaseField(true);
            this.setPrimaryField(column.primary());
            this.setKeywordField(column.keyword());
        }
    }

    @Override
    public void initJoinInfo(@NotNull VariableElement metaData, ModelElementInfo model) {
        Join join = metaData.getAnnotation(Join.class);
        if (Objects.nonNull(join)) {
            this.setJoin(join);
            this.setClassJoinField(this.initIsClassJoinField(metaData));
            this.setCollectionJoinField(this.initIsCollectionJoinField(metaData));
            this.setLeftModel(this.initLeftModel(metaData));
            this.setRightModel(this.initRightModel(metaData));
            this.setJoinField(true);
            this.setLeftSelectFieldNames(Arrays.asList(this.getJoin().leftSelectFields()));
            this.setLeftJoinField(this.getJoin().leftJoinField());
            this.setRightJoinField(this.getJoin().rightJoinField());
            this.setJoinType(this.getJoin().joinType());
            this.setJoinCondition(this.getJoin().joinCondition());
            this.setInfix(this.getJoin().infix());
        }
    }

    private boolean initIsClassJoinField(@NotNull VariableElement field) {
        return !this.initIsCollectionJoinField(field);
    }

    private boolean initIsCollectionJoinField(@NotNull VariableElement field) {
        return field.asType() instanceof DeclaredType temp && StrUtil.equals(temp.asElement().getSimpleName(), List.class.getSimpleName());
    }

    @Nullable
    private ModelElementInfo initLeftModel(@NotNull VariableElement field) {
        if (this.isCollectionJoinField() && field.asType() instanceof DeclaredType declaredType) {
            List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
            if (Objects.nonNull(typeArguments) && !typeArguments.isEmpty() && this.getModel().getTypeUtils().asElement(typeArguments.getFirst()) instanceof TypeElement temp) {
                return new ModelElementInfo(this.getModel().getModuleInfo(), temp, this.getModel().getTypeUtils(), true);
            }
            return null;
        } else {
            return new ModelElementInfo(this.getModel().getModuleInfo(), (TypeElement) this.getModel().getTypeUtils().asElement(field.asType()), this.getModel().getTypeUtils(), true);
        }
    }

    @Nullable
    private ModelElementInfo initRightModel(@NotNull VariableElement field) {
        try {
            Class<? extends BaseModel> unused = this.getJoin().rightTable();
        } catch (MirroredTypeException e) {
            TypeMirror typeMirror = e.getTypeMirror();
            if (this.getModel().getTypeUtils().asElement(typeMirror) instanceof TypeElement temp) {
                if (StrUtil.equals(temp.getSimpleName().toString(), BaseModel.class.getSimpleName())) {
                    return this.getModel();
                } else {
                    return new ModelElementInfo(this.getModel().getModuleInfo(), temp, this.getModel().getTypeUtils(), true);
                }
            }
        }
        return null;
    }

    @NotNull
    private String initJdbcType(@NotNull VariableElement field, @NotNull Column column) {
        String jdbcType = column.jdbcType();
        if (StrUtil.isBlank(jdbcType)) {
            switch (this.getModel().getDataBaseType()) {
                case mysql -> jdbcType = MysqlTypeMapInfo.getDbColumnTypeByJavaTypeName(this.getJavaTypeName());
                case postgresql -> jdbcType = PostgresqlTypeMapInfo.getDbColumnTypeByJavaTypeName(this.getJavaTypeName());
                case mssql -> jdbcType = MssqlTypeMapInfo.getDbColumnTypeByJavaTypeName(this.getJavaTypeName());
//                TODO 添加对应处理
                default -> throw new IllegalArgumentException("暂无对应数据库类型实现");
            }
        }
        return jdbcType;
    }

    private String initSqlStyleName(@NotNull VariableElement metaData, @NotNull ModelElementInfo model) {
        if (Objects.equals(model.getSqlStyle(), SqlStyle.sc)) {
            return StrUtil.toUnderlineCase(this.getName());
        }
        return StrUtil.lowerFirst(this.getName());
    }


}
