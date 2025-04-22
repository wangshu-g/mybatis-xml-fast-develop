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

import cn.hutool.core.util.StrUtil;
import com.wangshu.base.mapper.BaseDataMapper;
import com.wangshu.base.model.BaseModel;
import com.wangshu.exception.IException;
import com.wangshu.tool.CacheTool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangshu-g
 * <p>AbstractBaseDataService implements BaseService</p>
 */
public abstract class AbstractBaseDataService<P, M extends BaseDataMapper<T>, T extends BaseModel> implements BaseDataService<P, T> {

    public Logger log = LoggerFactory.getLogger(getClass());

    /**
     * <p>获取对应mapper</p>
     *
     * @return M extends BaseMapper<T extends BaseModel>
     **/
    public abstract M getMapper();

    /**
     * <p>保存</p>
     *
     * @param model extends {@link BaseModel}
     * @return BaseModel
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int _save(@NotNull T model) {
        Field modelPrimaryField = getModelPrimaryField();
        if (Objects.isNull(modelPrimaryField)) {
            log.error("实体类需要指定主键字段");
            throw new IException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Object primaryValue = model.safeModelAnyValueByFieldName(modelPrimaryField.getName());
        if (!StrUtil.isBlankIfStr(primaryValue) && Objects.nonNull(_select(modelPrimaryField.getName(), primaryValue))) {
            return _update(model);
        }
        model = saveParamFilter(model);
        if (saveValidate(model)) {
            return getMapper()._save(model);
        }
        throw new IException(HttpStatus.BAD_REQUEST);
    }

    /**
     * <p>保存</p>
     *
     * @param map {conditionName : value}
     * @return int
     **/
    @Transactional(rollbackFor = Exception.class)
    public int _save(@NotNull Map<String, Object> map) {
        T model;
        try {
            model = getModelClazz().getConstructor().newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            log.error("获取实体类实例失败,请检查泛型", e);
            throw new IException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        model.setModelValuesFromMapByFieldName(map);
        return _save(model);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int _saveUnCheckExist(T model) {
        Field modelPrimaryField = getModelPrimaryField();
        if (Objects.isNull(modelPrimaryField)) {
            log.error("实体类需要指定主键字段");
            throw new IException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Object primaryValue = model.safeModelAnyValueByFieldName(modelPrimaryField.getName());
        if (!StrUtil.isBlankIfStr(primaryValue)) {
            return _update(model);
        }
        model = saveParamFilter(model);
        if (saveValidate(model)) {
            return getMapper()._save(model);
        }
        throw new IException(HttpStatus.BAD_REQUEST);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int _saveUnCheckExist(@NotNull Map<String, Object> map) {
        T model;
        try {
            model = getModelClazz().getConstructor().newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            log.error("获取实体类实例失败,请检查泛型", e);
            throw new IException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        model.setModelValuesFromMapByFieldName(map);
        return _saveUnCheckExist(model);
    }

    public T saveParamFilter(@NotNull T model) {
        Field modelPrimaryField = getModelPrimaryField();
        if (Objects.isNull(modelPrimaryField)) {
            log.error("实体类需要指定主键字段");
            throw new IException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Object primaryValue = model.safeModelAnyValueByFieldName(modelPrimaryField.getName());
        if (Objects.isNull(primaryValue) && modelPrimaryField.getType().equals(String.class)) {
            model.setModelAnyValueByFieldName(modelPrimaryField.getName(), getUUID());
        }
        Field modelCreatedAtField = CacheTool.getModelCreatedAtField(getModelClazz());
        if (Objects.nonNull(modelCreatedAtField)) {
            model.setModelAnyValueByFieldName(modelCreatedAtField.getName(), new Date());
        }
        return model;
    }

    /**
     * <p>保存验证</p>
     *
     * @param model 实体类
     * @return boolean
     **/
    public boolean saveValidate(@NotNull T model) {
        return true;
    }

    /**
     * <p>批量更新</p>
     * <p>不会去校验是否存在</p>
     *
     * @param modelList 实体类列表
     * @return int
     **/
    @Override
    @Transactional
    public int _batchSave(@NotNull List<T> modelList) {
        List<T> newModelList = modelList.stream().map(model -> {
            model = saveParamFilter(model);
            return model;
        }).toList();
        if (newModelList.stream().allMatch(this::saveValidate)) {
            return getMapper()._batchSave(modelList);
        }
        throw new IException(HttpStatus.BAD_REQUEST);
    }

    /**
     * <p>删除</p>
     *
     * @param map {conditionName : value}
     * @return BaseModel
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int _delete(@NotNull Map<String, Object> map) {
        map = deleteParamFilter(map);
        if (deleteValidate(map)) {
            return getMapper()._delete(map);
        }
        throw new IException(HttpStatus.BAD_REQUEST);
    }

    @Transactional(rollbackFor = Exception.class)
    public int _delete(@NotNull Object... keyValuesArray) {
        return _delete(keyValuesArrayParamssafeToMap(keyValuesArray));
    }

    /**
     * <p>删除</p>
     *
     * @param model extends BaseModel
     * @return int
     **/
    @Transactional(rollbackFor = Exception.class)
    public int _delete(@NotNull T model) {
        return _delete(model.safeToMap());
    }

    /**
     * <p>删除</p>
     *
     * @param id id
     * @return int
     **/
    @Transactional(rollbackFor = Exception.class)
    public int _delete(P id) {
        if (Objects.isNull(id) || StrUtil.isBlank(id.toString())) {
            log.error("根据主键字段删除时主键字段不能为空,异常参数: {}", id);
            throw new IException(HttpStatus.BAD_REQUEST);
        }
        Map<String, Object> map = new HashMap<>(1);
        Field modelPrimaryField = getModelPrimaryField();
        if (Objects.isNull(modelPrimaryField)) {
            log.error("实体类需要指定主键字段");
            throw new IException(HttpStatus.BAD_REQUEST);
        }
        map.put(modelPrimaryField.getName(), id);
        return _delete(map);
    }

    public Map<String, Object> deleteParamFilter(@NotNull Map<String, Object> map) {
        if (map.isEmpty() || map.keySet().stream().noneMatch(CacheTool.getModelDeleteMethodPossibleWhereParameterName(getModelClazz())::contains)) {
            log.error("没有合法的删除参数!如场景需要,建议单独写一个方法(也可重写该验证方法,但不建议!),异常参数: {}", map);
            throw new IException(HttpStatus.BAD_REQUEST);
        }
        return map;
    }

    /**
     * <p>删除验证</p>
     *
     * @param map {conditionName : value}
     * @return boolean
     **/
    public boolean deleteValidate(@NotNull Map<String, Object> map) {
        return true;
    }

    /**
     * <p>软删除</p>
     *
     * @param map {conditionName : value}
     * @return int
     **/
    public int _softDelete(@NotNull Map<String, Object> map) {
        map = softDeleteParamFilter(map);
        if (softDeleteValidate(map)) {
            return getMapper()._update(map);
        }
        throw new IException(HttpStatus.BAD_REQUEST);
    }

    @Transactional(rollbackFor = Exception.class)
    public int _softDelete(P id) {
        if (Objects.isNull(id) || StrUtil.isBlank(id.toString())) {
            log.error("根据主键字段更新时主键字段不能为空,异常参数: {}", id);
            throw new IException(HttpStatus.BAD_REQUEST);
        }
        Field modelPrimaryField = getModelPrimaryField();
        if (Objects.isNull(modelPrimaryField)) {
            log.error("实体类需要指定主键字段");
            throw new IException(HttpStatus.BAD_REQUEST);
        }
        Map<String, Object> map = new HashMap<>();
        map.put(modelPrimaryField.getName(), id);
        return _softDelete(map);
    }

    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings("unchecked")
    public int _softDelete(@NotNull T model) {
        Field modelPrimaryField = getModelPrimaryField();
        if (Objects.isNull(modelPrimaryField)) {
            log.error("实体类需要指定主键字段");
            throw new IException(HttpStatus.BAD_REQUEST);
        }
        Object primaryValue = model.safeModelAnyValueByFieldName(modelPrimaryField.getName());
        if (Objects.isNull(primaryValue) || StrUtil.isBlank(primaryValue.toString())) {
            log.error("使用实体类更新时主键字段不能为空,异常参数: {}", primaryValue);
            throw new IException(HttpStatus.BAD_REQUEST);
        }
        return _softDelete((P) primaryValue);
    }

    @Transactional(rollbackFor = Exception.class)
    public int _softDelete(@NotNull Object... keyValuesArray) {
        return _softDelete(keyValuesArrayParamssafeToMap(keyValuesArray));
    }

    public Map<String, Object> softDeleteParamFilter(@NotNull Map<String, Object> map) {
        if (map.isEmpty() || map.keySet().stream().noneMatch(CacheTool.getModelUpdateMethodPossibleWhereParameterName(getModelClazz())::contains)) {
            log.error("没有合法的软删除参数!如场景需要,建议单独写一个方法(也可重写该验证方法,但不建议!),异常参数: {}", map);
            throw new IException(HttpStatus.BAD_REQUEST);
        }
        Field modelDeletedField = CacheTool.getModelDeletedField(getModelClazz());
        if (Objects.isNull(modelDeletedField)) {
            log.error("未找到 DeletedAt 标注的字段");
            throw new IException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String newDeletedAtName = StrUtil.concat(false, "new", StrUtil.upperFirst(modelDeletedField.getName()));
        if (Objects.isNull(map.get(newDeletedAtName))) {
            map.put(newDeletedAtName, new Date());
        }
        Field modelUpdatedField = CacheTool.getModelUpdatedField(getModelClazz());
        if (Objects.nonNull(modelUpdatedField)) {
            String newUpdatedAtName = StrUtil.concat(false, "new", StrUtil.upperFirst(modelUpdatedField.getName()));
            if (Objects.isNull(map.get(newUpdatedAtName))) {
                map.put(newUpdatedAtName, new Date());
            }
        }
        Field modelCreatedAtField = CacheTool.getModelCreatedAtField(getModelClazz());
        if (Objects.nonNull(modelCreatedAtField)) {
            String newCreatedAtName = StrUtil.concat(false, "new", StrUtil.upperFirst(modelCreatedAtField.getName()));
            map.remove(newCreatedAtName);
        }
        return map;
    }

    public boolean softDeleteValidate(@NotNull Map<String, Object> map) {
        if (map.keySet().stream().filter(key -> !key.startsWith("new")).toList().isEmpty()) {
            log.warn("更新条件参数没有有效新值,注意检查相关代码,详细参数: {}", map);
        }
        return true;
    }

    /**
     * <p>更新</p>
     *
     * @param map {conditionName : value}
     * @return int
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int _update(@NotNull Map<String, Object> map) {
        map = updateParamFilter(map);
        if (updateValidate(map)) {
            return getMapper()._update(map);
        }
        throw new IException(HttpStatus.BAD_REQUEST);
    }

    @Transactional(rollbackFor = Exception.class)
    public int _update(P id, @NotNull String column1, Object newValue) {
        if (Objects.isNull(id) || StrUtil.isBlank(id.toString())) {
            log.error("根据主键字段更新时主键字段不能为空,异常参数: {}", id);
            throw new IException(HttpStatus.BAD_REQUEST);
        }
        Map<String, Object> map = new HashMap<>();
        Field modelPrimaryField = getModelPrimaryField();
        if (Objects.isNull(modelPrimaryField)) {
            log.error("实体类需要指定主键字段");
            throw new IException(HttpStatus.BAD_REQUEST);
        }
        map.put(modelPrimaryField.getName(), id);
        if (!column1.startsWith("new")) {
            map.put(StrUtil.concat(false, "new", StrUtil.upperFirst(column1)), newValue);
        }
        return _update(map);
    }

    /**
     * <p>更新</p>
     * <p>防止更新参数和条件参数冲突,会强制将实体类所有字段添加new{列名}作为新值,并且只会保留id作为唯一更新条件</p>
     *
     * @param model extends BaseModel
     * @return int
     **/
    @Transactional(rollbackFor = Exception.class)
    public int _update(@NotNull T model) {
        Field modelPrimaryField = getModelPrimaryField();
        if (Objects.isNull(modelPrimaryField)) {
            log.error("实体类需要指定主键字段");
            throw new IException(HttpStatus.BAD_REQUEST);
        }
        Object primaryValue = model.safeModelAnyValueByFieldName(modelPrimaryField.getName());
        if (Objects.isNull(primaryValue) || StrUtil.isBlank(primaryValue.toString())) {
            log.error("使用实体类更新时主键字段不能为空,异常参数: {}", primaryValue);
            throw new IException(HttpStatus.BAD_REQUEST);
        }
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> temp = model.safeToMap();
        temp.forEach((k, v) -> param.put(StrUtil.concat(false, "new", StrUtil.upperFirst(k)), v));
        param.put(modelPrimaryField.getName(), primaryValue);
        log.warn("实体类更新.防止更新参数和条件参数冲突,参数强制修改为: {}", param);
        return _update(param);
    }

    @Transactional(rollbackFor = Exception.class)
    public int _update(@NotNull Object... keyValuesArray) {
        return _update(keyValuesArrayParamssafeToMap(keyValuesArray));
    }

    /**
     * <p>更新参数过滤,这里的new是防止更新条件和更新值参数冲突,只有更新操作强制这样做(有更好方案可重写替换)</p>
     *
     * @param map {conditionName : value}
     * @return Map<String, Object>
     **/
    public Map<String, Object> updateParamFilter(@NotNull Map<String, Object> map) {
        if (map.isEmpty() || map.keySet().stream().noneMatch(CacheTool.getModelUpdateMethodPossibleWhereParameterName(getModelClazz())::contains)) {
            log.error("没有合法的更新参数!如场景需要,建议单独写一个方法(也可重写该验证方法,但不建议!),异常参数: {}", map);
            throw new IException(HttpStatus.BAD_REQUEST);
        }
        Field modelUpdatedField = CacheTool.getModelUpdatedField(getModelClazz());
        if (Objects.nonNull(modelUpdatedField)) {
            String newUpdatedAtName = StrUtil.concat(false, "new", StrUtil.upperFirst(modelUpdatedField.getName()));
            if (Objects.isNull(map.get(newUpdatedAtName))) {
                map.put(newUpdatedAtName, new Date());
            }
        }
        Field modelCreatedAtField = CacheTool.getModelCreatedAtField(getModelClazz());
        if (Objects.nonNull(modelCreatedAtField)) {
            String newCreatedAtName = StrUtil.concat(false, "new", StrUtil.upperFirst(modelCreatedAtField.getName()));
            map.remove(newCreatedAtName);
        }
        return map;
    }

    /**
     * <p>更新验证</p>
     *
     * @param map {conditionName : value}
     * @return boolean
     **/
    public boolean updateValidate(@NotNull Map<String, Object> map) {
        if (map.keySet().stream().filter(key -> !key.startsWith("new")).toList().isEmpty()) {
            log.warn("更新条件参数没有有效新值,注意检查相关代码,详细参数: {}", map);
        }
        return true;
    }

    /**
     * <p>查询1条</p>
     *
     * @param map {conditionName : value}
     * @return T extends BaseModel
     **/
    @Override
    public @Nullable T _select(@NotNull Map<String, Object> map) {
        map = selectParamFilter(map);
        if (selectValidate(map)) {
            try {
                return getMapper()._select(map);
            } catch (MyBatisSystemException e) {
                throw new IException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        throw new IException(HttpStatus.BAD_REQUEST);
    }

    public Map<String, Object> selectParamFilter(@NotNull Map<String, Object> map) {
        if (map.isEmpty()) {
            throw new IException(HttpStatus.BAD_REQUEST);
        }
        return map;
    }

    public boolean selectValidate(@NotNull Map<String, Object> map) {
        return true;
    }

    /**
     * <p>查询1条</p>
     *
     * @param model extends BaseModel
     * @return T extends BaseModel
     **/
    public @Nullable T _select(@NotNull T model) {
        return _select(model.safeToMap());
    }

    /**
     * <p>查询1条</p>
     *
     * @param id id
     * @return T extends BaseModel
     **/
    public @Nullable T _select(P id) {
        if (Objects.isNull(id) || StrUtil.isBlank(id.toString())) {
            log.error("根据主键字段查询时主键字段不能为空,异常参数: {}", id);
            throw new IException(HttpStatus.BAD_REQUEST);
        }
        Map<String, Object> map = new HashMap<>();
        Field modelPrimaryField = getModelPrimaryField();
        if (Objects.isNull(modelPrimaryField)) {
            log.error("实体类需要指定主键字段");
            throw new IException(HttpStatus.BAD_REQUEST);
        }
        map.put(modelPrimaryField.getName(), id);
        return _select(map);
    }

    /**
     * <p>查询1条</p>
     *
     * @param column1 conditionName
     * @param param1  value
     * @param column2 conditionName
     * @param param2  value
     * @return T extends BaseModel
     **/
    public @Nullable T _select(String column1, Object param1, String column2, Object param2) {
        Map<String, Object> map = new HashMap<>(1);
        map.put(column1, param1);
        map.put(column2, param2);
        return _select(map);
    }

    public @Nullable T _select(@NotNull Object... keyValuesArray) {
        return _select(keyValuesArrayParamssafeToMap(keyValuesArray));
    }

    /**
     * <p>查询列表</p>
     *
     * @param map {conditionName : value}
     * @return List<Map < String, Object>>
     **/
    @Override
    public @NotNull List<Map<String, Object>> _getList(@NotNull Map<String, Object> map) {
        map = listParamFilter(map);
        if (listValidate(map)) {
            return getMapper()._getList(map);
        }
        throw new IException(HttpStatus.BAD_REQUEST);
    }

    /**
     * <p>查询列表,忽略分页</p>
     *
     * @param map {conditionName : value}
     * @return List<Map < String, Object>>
     **/
    public @NotNull List<Map<String, Object>> _getListWithOutLimit(@NotNull Map<String, Object> map) {
        map = listParamFilter(map);
        if (listValidate(map)) {
            map.remove("pageIndex");
            map.remove("pageSize");
            return getMapper()._getList(map);
        }
        throw new IException(HttpStatus.BAD_REQUEST);
    }

    public @NotNull List<Map<String, Object>> _getListWithOutLimit(String column, Object value) {
        Map<String, Object> map = new HashMap<>(1);
        map.put(column, value);
        return _getListWithOutLimit(map);
    }

    public @NotNull List<Map<String, Object>> _getListWithOutLimit(@NotNull T model) {
        return _getListWithOutLimit(model.safeToMap());
    }

    public @NotNull List<Map<String, Object>> _getListWithOutLimit(@NotNull Object... keyValuesArray) {
        return _getListWithOutLimit(keyValuesArrayParamssafeToMap(keyValuesArray));
    }

    /**
     * <p>查询列表</p>
     *
     * @param model extends BaseModel
     * @return List<Map < String, Object>>
     **/
    public @NotNull List<Map<String, Object>> _getList(@NotNull T model) {
        return _getList(model.safeToMap());
    }

    /**
     * <p>查询列表</p>
     *
     * @param column column
     * @param value  value
     * @return List<Map < String, Object>>
     **/
    public @NotNull List<Map<String, Object>> _getList(String column, Object value) {
        Map<String, Object> map = new HashMap<>(1);
        map.put(column, value);
        return _getList(map);
    }

    /**
     * <p>查询列表</p>
     *
     * @param keyValues 键值对,偶数长度。key, value, key, value...
     * @return List<Map < String, Object>>
     **/
    public @NotNull List<Map<String, Object>> _getList(@NotNull Object... keyValues) {
        Map<String, Object> map = new HashMap<>();
        if (keyValues.length > 0) {
            int length = keyValues.length;
            for (int i = 0; i < keyValues.length; i++) {
                if (i % 2 == 0 && length >= i + 1) {
                    map.put(String.valueOf(keyValues[i]), keyValues[i + 1]);
                }
            }
            return _getList(map);
        }
        return _getList(map);
    }

    @Override
    public @NotNull List<T> _getNestList(@NotNull Map<String, Object> map) {
        map = listParamFilter(map);
        if (listValidate(map)) {
            return getMapper()._getNestList(map);
        }
        throw new IException(HttpStatus.BAD_REQUEST);
    }

    public @NotNull List<T> _getNestList(@NotNull T model) {
        return _getNestList(model.safeToMap());
    }

    public @NotNull List<T> _getNestList(String column, Object value) {
        Map<String, Object> map = new HashMap<>(1);
        map.put(column, value);
        return _getNestList(map);
    }

    public @NotNull List<T> _getNestList(@NotNull Object... keyValuesArray) {
        return _getNestList(keyValuesArrayParamssafeToMap(keyValuesArray));
    }

    public @NotNull List<T> _getNestListWithOutLimit(@NotNull Map<String, Object> map) {
        map = listParamFilter(map);
        if (listValidate(map)) {
            map.remove("pageIndex");
            map.remove("pageSize");
            return getMapper()._getNestList(map);
        }
        throw new IException(HttpStatus.BAD_REQUEST);
    }

    public @NotNull List<T> _getNestListWithOutLimit(String column, Object value) {
        Map<String, Object> map = new HashMap<>(1);
        map.put(column, value);
        return _getNestListWithOutLimit(map);
    }

    public @NotNull List<T> _getNestListWithOutLimit(@NotNull T model) {
        return _getNestListWithOutLimit(model.safeToMap());
    }

    public @NotNull List<T> _getNestListWithOutLimit(@NotNull Object... keyValuesArray) {
        return _getNestListWithOutLimit(keyValuesArrayParamssafeToMap(keyValuesArray));
    }

    public Map<String, Object> listParamFilter(@NotNull Map<String, Object> map) {
        long pageIndex;
        try {
            pageIndex = Long.parseLong(String.valueOf(map.get("pageIndex")));
            if (pageIndex <= 0) {
                pageIndex = 1;
            }
        } catch (NumberFormatException e) {
            log.warn("不规范的 pageIndex 参数,详细参数: {}", map.get("pageIndex"));
            pageIndex = 1;
        }
        long pageSize;
        try {
            pageSize = Long.parseLong(String.valueOf(map.get("pageSize")));
            if (pageSize <= 0) {
                pageSize = 10;
            }
        } catch (NumberFormatException e) {
            log.warn("不规范的 pageSize 参数,详细参数: {}", map.get("pageSize"));
            pageSize = 10;
        }
        map.put("pageIndex", (pageIndex - 1) * pageSize);
        map.put("pageSize", pageSize);
        String orderColumn = String.valueOf(Objects.isNull(map.get("orderColumn")) ? "" : map.get("orderColumn"));
        if (!CacheTool.getModelOrderColumnPossibleParameterName(getModelClazz()).contains(orderColumn)) {
            map.remove("orderColumn");
            log.warn("orderColumn 参数无效,详细参数: {}", orderColumn);
            Field modelDefaultOrderField = CacheTool.getModelDefaultOrderField(getModelClazz());
            if (Objects.nonNull(modelDefaultOrderField)) {
                orderColumn = modelDefaultOrderField.getName();
                log.warn("存在 defaultOrderField,orderColumn 参数重设为: {}", orderColumn);
            }
        }
        if (StrUtil.isNotBlank(orderColumn)) {
            map.put("orderColumn", orderColumn);
            String order = String.valueOf(Objects.isNull(map.get("order")) ? "" : map.get("order"));
            if (!StrUtil.equalsIgnoreCase(order, "asc") && !StrUtil.equalsIgnoreCase(order, "desc")) {
                map.put("order", "asc");
            }
        }
        return map;
    }

    public boolean listValidate(@NotNull Map<String, Object> map) {
        return true;
    }

    @Override
    public int _getTotal(@NotNull Map<String, Object> map) {
        return getMapper()._getTotal(map);
    }

    public int _getTotal(@NotNull Object... keyValuesArray) {
        return _getTotal(keyValuesArrayParamssafeToMap(keyValuesArray));
    }

    public int _getTotal() {
        return _getTotal(Map.of());
    }

    @SuppressWarnings("unchecked")
    public Class<T> getModelClazz() {
        return (Class<T>) CacheTool.getServiceModelGeneric(getClass());
    }

    @SuppressWarnings("unchecked")
    public Class<M> getMapperClazz() {
        return (Class<M>) CacheTool.getServiceMapperGeneric(getClass());
    }

    public List<Field> getModelFields() {
        return CacheTool.getServiceModelGenericFields(getClass());
    }

    public List<Field> getModelBaseFields() {
        return CacheTool.getServiceModelGenericBaseFields(getClass());
    }

    public Map<String, Field> getModelMapBaseFields() {
        return getModelBaseFields().stream().collect(Collectors.toMap(Field::getName, value -> value));
    }

    public Field getModelPrimaryField() {
        return CacheTool.getModelPrimaryField(getModelClazz());
    }

    public Map<String, Object> keyValuesArrayParamssafeToMap(@NotNull Object... keyValuesArray) {
        Map<String, Object> map = new HashMap<>();
        int length = keyValuesArray.length;
        for (int i = 0; i < keyValuesArray.length; i++) {
            if (i % 2 == 0 && length >= i + 1) {
                map.put(String.valueOf(keyValuesArray[i]), keyValuesArray[i + 1]);
            }
        }
        return map;
    }

}