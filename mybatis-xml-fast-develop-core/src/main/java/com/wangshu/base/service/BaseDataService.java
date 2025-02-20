package com.wangshu.base.service;

import com.wangshu.base.mapper.BaseDataMapper;
import com.wangshu.base.model.BaseModel;

import java.util.List;
import java.util.Map;

/**
 * @author GSF
 * <p>BaseService</p>
 */
public interface BaseDataService<P, M extends BaseDataMapper<T>, T extends BaseModel> extends BaseService {

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
