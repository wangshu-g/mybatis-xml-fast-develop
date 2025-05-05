package com.wangshu.base.service;

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

import com.wangshu.base.model.BaseModel;
import com.wangshu.base.query.CommonQueryParam;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * @author wangshu-g
 * <p>BaseService</p>
 */
public interface BaseDataService<P, T extends BaseModel> extends BaseService {

    /**
     * <p>保存</p>
     *
     * @param model 实体类
     **/
    int _save(@NotNull T model);

    int _save(@NotNull Map<String, Object> map);

    int _saveUnCheckExist(T model);

    int _saveUnCheckExist(@NotNull Map<String, Object> map);

    /**
     * <p>批量保存</p>
     *
     * @param modelList 实体类列表
     **/
    int _batchSave(@NotNull List<T> modelList);

    /**
     * <p>删除</p>
     *
     * @param map {columnName : value}
     **/
    int _delete(@NotNull Map<String, Object> map);

    int _delete(@NotNull Object... keyValuesArray);

    int _delete(@NotNull T model);

    int _delete(P id);

    /**
     * <p>更新</p>
     *
     * @param map {columnName : value}
     **/
    int _update(@NotNull Map<String, Object> map);

    int _update(P id, String column1, Object newValue);

    int _update(@NotNull T model);

    int _update(@NotNull Object... keyValuesArray);

    /**
     * <p>软删除</p>
     *
     * @param map {conditionName : value}
     * @return int
     **/
    int _softDelete(@NotNull Map<String, Object> map);

    int _softDelete(P id);

    int _softDelete(@NotNull T model);

    int _softDelete(@NotNull Object... keyValuesArray);

    /**
     * <p>查询1条</p>
     *
     * @param map {columnName : value}
     **/
    @Nullable T _select(@NotNull Map<String, Object> map);

    @Nullable T _select(@NotNull T model);

    @Nullable T _select(@NotNull CommonQueryParam<T> query);

    @Nullable T _select(P id);

    @Nullable T _select(String column1, Object param1, String column2, Object param2);

    @Nullable T _select(@NotNull Object... keyValuesArray);

    /**
     * <p>查询列表</p>
     *
     * @param map {columnName : value}
     **/
    @NotNull List<Map<String, Object>> _getList(@NotNull Map<String, Object> map);

    @NotNull List<Map<String, Object>> _getListWithOutLimit(@NotNull Map<String, Object> map);

    @NotNull List<Map<String, Object>> _getListWithOutLimit(String column, Object value);

    @NotNull List<Map<String, Object>> _getListWithOutLimit(@NotNull T model);

    @NotNull List<Map<String, Object>> _getListWithOutLimit(@NotNull CommonQueryParam<T> query);

    @NotNull List<Map<String, Object>> _getListWithOutLimit(@NotNull Object... keyValuesArray);

    @NotNull List<Map<String, Object>> _getList(@NotNull T model);

    @NotNull List<Map<String, Object>> _getList(@NotNull CommonQueryParam<T> query);

    @NotNull List<Map<String, Object>> _getList(String column, Object value);

    @NotNull List<Map<String, Object>> _getList(@NotNull Object... keyValues);

    /**
     * <p>查询嵌套列表</p>
     *
     * @param map {columnName : value}
     **/
    @NotNull List<T> _getNestList(@NotNull Map<String, Object> map);

    @NotNull List<T> _getNestListWithOutLimit(@NotNull Map<String, Object> map);

    @NotNull List<T> _getNestListWithOutLimit(String column, Object value);

    @NotNull List<T> _getNestListWithOutLimit(@NotNull T model);

    @NotNull List<T> _getNestListWithOutLimit(@NotNull CommonQueryParam<T> query);

    @NotNull List<T> _getNestListWithOutLimit(@NotNull Object... keyValuesArray);

    @NotNull List<T> _getNestList(@NotNull T model);

    @NotNull List<T> _getNestList(@NotNull CommonQueryParam<T> query);

    @NotNull List<T> _getNestList(String column, Object value);

    @NotNull List<T> _getNestList(@NotNull Object... keyValues);

    int _getTotal(@NotNull Map<String, Object> map);

    int _getTotal(@NotNull Object... keyValuesArray);

    int _getTotal();

}
