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

import com.wangshu.base.mapper.BaseDataMapper;
import com.wangshu.base.model.BaseModel;

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
    int save(T model);

    int save(Map<String, Object> map);

    /**
     * <p>批量保存</p>
     *
     * @param modelList 实体类列表
     **/
    int batchSave(List<T> modelList);

    /**
     * <p>删除</p>
     *
     * @param map {columnName : value}
     **/
    int delete(Map<String, Object> map);

    int delete(Object... keyValuesArray);

    int delete(T model);

    int delete(P id);

    /**
     * <p>更新</p>
     *
     * @param map {columnName : value}
     **/
    int update(Map<String, Object> map);

    int update(P id, String column1, Object newValue);

    int update(T model);

    int update(Object... keyValuesArray);

    /**
     * <p>查询1条</p>
     *
     * @param map {columnName : value}
     **/
    T select(Map<String, Object> map);

    T select(T model);

    T select(P id);

    T select(String column1, Object param1, String column2, Object param2);

    T select(Object... keyValuesArray);

    /**
     * <p>查询列表</p>
     *
     * @param map {columnName : value}
     **/
    List<Map<String, Object>> getList(Map<String, Object> map);

    List<Map<String, Object>> getListWithOutLimit(Map<String, Object> map);

    List<Map<String, Object>> getListWithOutLimit(String column, Object value);

    List<Map<String, Object>> getListWithOutLimit(T model);

    List<Map<String, Object>> getListWithOutLimit(Object... keyValuesArray);

    List<Map<String, Object>> getList(T model);

    List<Map<String, Object>> getList(String column, Object value);

    List<Map<String, Object>> getList(Object... keyValues);

    /**
     * <p>查询嵌套列表</p>
     *
     * @param map {columnName : value}
     **/
    List<T> getNestList(Map<String, Object> map);

    List<T> getNestListWithOutLimit(Map<String, Object> map);

    List<T> getNestListWithOutLimit(String column, Object value);

    List<T> getNestListWithOutLimit(T model);

    List<T> getNestListWithOutLimit(Object... keyValuesArray);

    List<T> getNestList(T model);

    List<T> getNestList(String column, Object value);

    List<T> getNestList(Object... keyValues);

    int getTotal(Map<String, Object> map);

    int getTotal(Object... keyValuesArray);

    int getTotal();

}
