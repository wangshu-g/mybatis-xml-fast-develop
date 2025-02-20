package com.wangshu.base.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSON;
import com.wangshu.annotation.Column;
import com.wangshu.annotation.Data;
import com.wangshu.base.controller.daoru.ModelDataListener;
import com.wangshu.base.mapper.BaseDataMapper;
import com.wangshu.base.model.BaseModel;
import com.wangshu.enu.CommonErrorInfo;
import com.wangshu.exception.IException;
import com.wangshu.tool.CacheTool;
import com.wangshu.tool.ExcelUtil;
import com.wangshu.tool.StringUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author GSF
 * <p>AbstractBaseDataService implements BaseService</p>
 */
public abstract class AbstractBaseDataService<P, M extends BaseDataMapper<T>, T extends BaseModel> implements BaseDataService<P, M, T> {

    public Logger log = LoggerFactory.getLogger(this.getClass());

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
    public int save(@NotNull T model) {
        Field modelPrimaryField = this.getModelPrimaryField();
        if (Objects.isNull(modelPrimaryField)) {
            log.error("实体类需要指定主键字段");
            throw new IException(CommonErrorInfo.SERVER_ERROR);
        }
        Object primaryValue = model.modelAnyValueByFieldName(modelPrimaryField.getName());
        if (StringUtil.isNotEmpty(primaryValue) && Objects.nonNull(this.select(modelPrimaryField.getName(), primaryValue))) {
            return this.update(model);
        }
        model = this.saveParamFilter(model);
        if (this.saveValidate(model)) {
            return this.getMapper()._save(model);
        }
        throw new IException(CommonErrorInfo.BODY_NOT_MATCH);
    }

    /**
     * <p>保存</p>
     *
     * @param map {conditionName : value}
     * @return int
     **/
    @Transactional(rollbackFor = Exception.class)
    public int save(@NotNull Map<String, Object> map) {
        T model;
        try {
            model = this.getModelClazz().getConstructor().newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            log.error("获取实体类实例失败,请检查泛型", e);
            throw new IException(CommonErrorInfo.SERVER_ERROR);
        }
        model.setModelValuesFromMapByFieldName(map);
        return this.save(model);
    }

    public T saveParamFilter(@NotNull T model) {
        Field modelPrimaryField = this.getModelPrimaryField();
        if (Objects.isNull(modelPrimaryField)) {
            log.error("实体类需要指定主键字段");
            throw new IException(CommonErrorInfo.SERVER_ERROR);
        }
        if (StringUtil.isEmpty(model.modelAnyValueByFieldName(modelPrimaryField.getName())) && modelPrimaryField.getType().equals(String.class)) {
            model.setModelAnyValueByFieldName(modelPrimaryField.getName(), this.getId());
        }
        if (model.fieldIsExist("createdAt") && Objects.isNull(model.modelAnyValueByFieldName("createdAt"))) {
            model.setModelAnyValueByFieldName("createdAt", new Date());
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
        log.info("保存操作参数: {}", model.toJson());
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
    public int batchSave(@NotNull List<T> modelList) {
        List<T> newModelList = modelList.stream().map(model -> {
            model = this.saveParamFilter(model);
            return model;
        }).toList();
        if (newModelList.stream().allMatch(this::saveValidate)) {
            return this.getMapper()._batchSave(modelList);
        }
        throw new IException(CommonErrorInfo.BODY_NOT_MATCH);
    }

    /**
     * <p>删除</p>
     *
     * @param map {conditionName : value}
     * @return BaseModel
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(@NotNull Map<String, Object> map) {
        map = this.deleteParamFilter(map);
        if (this.deleteValidate(map)) {
            return this.getMapper()._delete(map);
        }
        throw new IException(CommonErrorInfo.BODY_NOT_MATCH);
    }

    @Transactional(rollbackFor = Exception.class)
    public int delete(@NotNull Object... keyValuesArray) {
        return this.delete(this.keyValuesArrayParamsToMap(keyValuesArray));
    }

    /**
     * <p>删除</p>
     *
     * @param model extends BaseModel
     * @return int
     **/
    @Transactional(rollbackFor = Exception.class)
    public int delete(@NotNull T model) {
        return this.delete(model.toMap());
    }

    /**
     * <p>删除</p>
     *
     * @param id id
     * @return int
     **/
    @Transactional(rollbackFor = Exception.class)
    public int delete(P id) {
        if (StringUtil.isEmpty(id)) {
            log.error("根据主键字段删除时主键字段不能为空,异常参数: {}", id);
            throw new IException(CommonErrorInfo.BODY_NOT_MATCH);
        }
        Map<String, Object> map = new HashMap<>(1);
        Field modelPrimaryField = this.getModelPrimaryField();
        if (Objects.isNull(modelPrimaryField)) {
            log.error("实体类需要指定主键字段");
            throw new IException(CommonErrorInfo.SERVER_ERROR);
        }
        map.put(modelPrimaryField.getName(), id);
        return this.delete(map);
    }

    public Map<String, Object> deleteParamFilter(@NotNull Map<String, Object> map) {
        if (map.isEmpty() || map.keySet().stream().noneMatch(CacheTool.getModelDeleteMethodPossibleWhereParameterName(this.getModelClazz())::contains)) {
            log.error("没有合法的删除参数!如场景需要,建议单独写一个方法(也可重写该验证方法,但不建议!),异常参数: {}", map);
            throw new IException(CommonErrorInfo.BODY_NOT_MATCH);
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
        log.info("删除操作参数: {}", JSON.toJSONString(map));
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
    public int update(@NotNull Map<String, Object> map) {
        map = updateParamFilter(map);
        if (this.updateValidate(map)) {
            return this.getMapper()._update(map);
        }
        throw new IException(CommonErrorInfo.BODY_NOT_MATCH);
    }

    @Transactional(rollbackFor = Exception.class)
    public int update(P id, @NotNull String column1, Object newValue) {
        if (StringUtil.isEmpty(id)) {
            log.error("根据主键字段更新时主键字段不能为空,异常参数: {}", id);
            throw new IException(CommonErrorInfo.BODY_NOT_MATCH);
        }
        Map<String, Object> map = new HashMap<>();
        Field modelPrimaryField = this.getModelPrimaryField();
        if (Objects.isNull(modelPrimaryField)) {
            log.error("实体类需要指定主键字段");
            throw new IException(CommonErrorInfo.SERVER_ERROR);
        }
        map.put(modelPrimaryField.getName(), id);
        if (!column1.startsWith("new")) {
            map.put(StringUtil.concat("new", StrUtil.upperFirst(column1)), newValue);
        }
        return this.update(map);
    }

    /**
     * <p>更新</p>
     * <p>防止更新参数和条件参数冲突,会强制将实体类所有字段添加new{列名}作为新值,并且只会保留id作为唯一更新条件</p>
     *
     * @param model extends BaseModel
     * @return int
     **/
    @Transactional(rollbackFor = Exception.class)
    public int update(@NotNull T model) {
        Field modelPrimaryField = this.getModelPrimaryField();
        if (Objects.isNull(modelPrimaryField)) {
            log.error("实体类需要指定主键字段");
            throw new IException(CommonErrorInfo.SERVER_ERROR);
        }
        Object primaryValue = model.modelAnyValueByFieldName(modelPrimaryField.getName());
        if (StringUtil.isEmpty(primaryValue)) {
            log.error("使用实体类更新时主键字段不能为空,异常参数: {}", primaryValue);
            throw new IException(CommonErrorInfo.BODY_NOT_MATCH);
        }
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> temp = model.toMap();
        temp.forEach((k, v) -> param.put(StringUtil.concat("new", StrUtil.upperFirst(k)), v));
        param.put(modelPrimaryField.getName(), primaryValue);
        log.warn("实体类更新.防止更新参数和条件参数冲突,参数强制修改为: {}", param);
        return this.update(param);
    }

    @Transactional(rollbackFor = Exception.class)
    public int update(@NotNull Object... keyValuesArray) {
        return this.update(this.keyValuesArrayParamsToMap(keyValuesArray));
    }

    /**
     * <p>更新参数过滤,这里的new是防止更新条件和更新值参数冲突,只有更新操作强制这样做(有更好方案可重写替换)</p>
     *
     * @param map {conditionName : value}
     * @return Map<String, Object>
     **/
    public Map<String, Object> updateParamFilter(@NotNull Map<String, Object> map) {
        if (map.isEmpty() || map.keySet().stream().noneMatch(CacheTool.getModelUpdateMethodPossibleWhereParameterName(this.getModelClazz())::contains)) {
            log.error("没有合法的更新参数!如场景需要,建议单独写一个方法(也可重写该验证方法,但不建议!),异常参数: {}", map);
            throw new IException(CommonErrorInfo.BODY_NOT_MATCH);
        }
        if (StringUtil.isEmpty(map.get("newUpdatedAt"))) {
            map.put("newUpdatedAt", new Date());
        }
        map.remove("newCreatedAt");
        return map;
    }

    /**
     * <p>更新验证</p>
     *
     * @param map {conditionName : value}
     * @return boolean
     **/
    public boolean updateValidate(@NotNull Map<String, Object> map) {
        log.info("更新操作参数: {}", map);
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
    public @Nullable T select(@NotNull Map<String, Object> map) {
        map = this.selectParamFilter(map);
        if (this.selectValidate(map)) {
            try {
                return this.getMapper()._select(map);
            } catch (MyBatisSystemException e) {
                log.error("异常: ", e);
                throw new IException(CommonErrorInfo.SERVER_ERROR);
            }
        }
        throw new IException(CommonErrorInfo.BODY_NOT_MATCH);
    }

    public Map<String, Object> selectParamFilter(@NotNull Map<String, Object> map) {
        if (map.isEmpty()) {
            throw new IException(CommonErrorInfo.BODY_NOT_MATCH);
        }
        return map;
    }

    public boolean selectValidate(@NotNull Map<String, Object> map) {
        log.info("查询操作参数: {}", JSON.toJSONString(map));
        return true;
    }

    /**
     * <p>查询1条</p>
     *
     * @param model extends BaseModel
     * @return T extends BaseModel
     **/
    public @Nullable T select(@NotNull T model) {
        return this.select(model.toMap());
    }

    /**
     * <p>查询1条</p>
     *
     * @param id id
     * @return T extends BaseModel
     **/
    public @Nullable T select(P id) {
        if (StringUtil.isEmpty(id)) {
            log.error("根据主键字段查询时主键字段不能为空,异常参数: {}", id);
            throw new IException(CommonErrorInfo.BODY_NOT_MATCH);
        }
        Map<String, Object> map = new HashMap<>();
        Field modelPrimaryField = this.getModelPrimaryField();
        if (Objects.isNull(modelPrimaryField)) {
            log.error("实体类需要指定主键字段");
            throw new IException(CommonErrorInfo.SERVER_ERROR);
        }
        map.put(modelPrimaryField.getName(), id);
        return this.select(map);
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
    public @Nullable T select(String column1, Object param1, String column2, Object param2) {
        Map<String, Object> map = new HashMap<>(1);
        map.put(column1, param1);
        map.put(column2, param2);
        return this.select(map);
    }

    public @Nullable T select(@NotNull Object... keyValuesArray) {
        return this.select(this.keyValuesArrayParamsToMap(keyValuesArray));
    }

    /**
     * <p>查询列表</p>
     *
     * @param map {conditionName : value}
     * @return List<Map < String, Object>>
     **/
    @Override
    public @NotNull List<Map<String, Object>> getList(@NotNull Map<String, Object> map) {
        map = this.listParamFilter(map);
        if (this.listValidate(map)) {
            return this.getMapper()._getList(map);
        }
        throw new IException(CommonErrorInfo.BODY_NOT_MATCH);
    }

    /**
     * <p>查询列表,忽略分页</p>
     *
     * @param map {conditionName : value}
     * @return List<Map < String, Object>>
     **/
    public @NotNull List<Map<String, Object>> getListWithOutLimit(@NotNull Map<String, Object> map) {
        map = this.listParamFilter(map);
        if (this.listValidate(map)) {
            map.remove("pageIndex");
            map.remove("pageSize");
            return this.getMapper()._getList(map);
        }
        throw new IException(CommonErrorInfo.BODY_NOT_MATCH);
    }

    public @NotNull List<Map<String, Object>> getListWithOutLimit(String column, Object value) {
        Map<String, Object> map = new HashMap<>(1);
        map.put(column, value);
        return this.getListWithOutLimit(map);
    }

    public @NotNull List<Map<String, Object>> getListWithOutLimit(@NotNull T model) {
        return this.getListWithOutLimit(model.toMap());
    }

    public @NotNull List<Map<String, Object>> getListWithOutLimit(@NotNull Object... keyValuesArray) {
        return this.getListWithOutLimit(this.keyValuesArrayParamsToMap(keyValuesArray));
    }

    /**
     * <p>查询列表</p>
     *
     * @param model extends BaseModel
     * @return List<Map < String, Object>>
     **/
    public @NotNull List<Map<String, Object>> getList(@NotNull T model) {
        return this.getList(model.toMap());
    }

    /**
     * <p>查询列表</p>
     *
     * @param column column
     * @param value  value
     * @return List<Map < String, Object>>
     **/
    public @NotNull List<Map<String, Object>> getList(String column, Object value) {
        Map<String, Object> map = new HashMap<>(1);
        map.put(column, value);
        return this.getList(map);
    }

    /**
     * <p>查询列表</p>
     *
     * @param keyValues 键值对,偶数长度。key, value, key, value...
     * @return List<Map < String, Object>>
     **/
    public @NotNull List<Map<String, Object>> getList(@NotNull Object... keyValues) {
        Map<String, Object> map = new HashMap<>();
        if (keyValues.length > 0) {
            int length = keyValues.length;
            for (int i = 0; i < keyValues.length; i++) {
                if (i % 2 == 0 && length >= i + 1) {
                    map.put(String.valueOf(keyValues[i]), keyValues[i + 1]);
                }
            }
            return this.getList(map);
        }
        return this.getList(map);
    }

    @Override
    public @NotNull List<T> getNestList(@NotNull Map<String, Object> map) {
        map = this.listParamFilter(map);
        if (this.listValidate(map)) {
            return this.getMapper()._getNestList(map);
        }
        throw new IException(CommonErrorInfo.BODY_NOT_MATCH);
    }

    public @NotNull List<T> getNestList(@NotNull T model) {
        return this.getNestList(model.toMap());
    }

    public @NotNull List<T> getNestList(String column, Object value) {
        Map<String, Object> map = new HashMap<>(1);
        map.put(column, value);
        return this.getNestList(map);
    }

    public @NotNull List<T> getNestList(@NotNull Object... keyValuesArray) {
        return this.getNestList(this.keyValuesArrayParamsToMap(keyValuesArray));
    }

    public @NotNull List<T> getNestListWithOutLimit(@NotNull Map<String, Object> map) {
        map = this.listParamFilter(map);
        if (this.listValidate(map)) {
            map.remove("pageIndex");
            map.remove("pageSize");
            return this.getMapper()._getNestList(map);
        }
        throw new IException(CommonErrorInfo.BODY_NOT_MATCH);
    }

    public @NotNull List<T> getNestListWithOutLimit(String column, Object value) {
        Map<String, Object> map = new HashMap<>(1);
        map.put(column, value);
        return this.getNestListWithOutLimit(map);
    }

    public @NotNull List<T> getNestListWithOutLimit(@NotNull T model) {
        return this.getNestListWithOutLimit(model.toMap());
    }

    public @NotNull List<T> getNestListWithOutLimit(@NotNull Object... keyValuesArray) {
        return this.getNestListWithOutLimit(this.keyValuesArrayParamsToMap(keyValuesArray));
    }

    public Map<String, Object> listParamFilter(@NotNull Map<String, Object> map) {
        long pageIndex;
        try {
            pageIndex = Long.parseLong(String.valueOf(map.get("pageIndex")));
            if (pageIndex <= 0) {
                pageIndex = 1;
            }
        } catch (NumberFormatException e) {
            log.warn("不规范的pageIndex参数,详细参数: {}", map.get("pageIndex"));
            pageIndex = 1;
        }
        long pageSize;
        try {
            pageSize = Long.parseLong(String.valueOf(map.get("pageSize")));
            if (pageSize <= 0) {
                pageSize = 10;
            }
        } catch (NumberFormatException e) {
            log.warn("不规范的pageSize参数,详细参数: {}", map.get("pageSize"));
            pageSize = 10;
        }
        map.put("pageIndex", (pageIndex - 1) * pageSize);
        map.put("pageSize", pageSize);
        String orderColumn = String.valueOf(map.get("orderColumn"));
        if (StringUtil.isNotEmpty(orderColumn)) {
            if (CacheTool.getModelOrderColumnPossibleParameterName(this.getModelClazz()).contains(orderColumn)) {
                String order = String.valueOf(map.get("order"));
                if (!StrUtil.equalsIgnoreCase(order, "asc") && !StrUtil.equalsIgnoreCase(order, "desc")) {
                    map.put("order", "asc");
                }
            } else {
                map.remove("orderColumn");
                log.warn("orderColumn参数无效,详细参数: {}", orderColumn);
            }
        }
        return map;
    }

    public boolean listValidate(@NotNull Map<String, Object> map) {
        return true;
    }

    @Override
    public int getTotal(@NotNull Map<String, Object> map) {
        return this.getMapper()._getTotal(map);
    }

    public int getTotal(@NotNull Object... keyValuesArray) {
        return this.getTotal(this.keyValuesArrayParamsToMap(keyValuesArray));
    }

    public int getTotal() {
        return this.getTotal(Map.of());
    }

    @Transactional
    public void importExcel(@NotNull MultipartFile multipartFile, @NotNull Integer headerRowNumber) {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            ModelDataListener modelDataListener = new ModelDataListener();
            EasyExcel.read(inputStream, modelDataListener).headRowNumber(headerRowNumber).doReadAllSync();
            List<T> modelData = modelDataListener.mapToModelData(this.getModelClazz());
            for (int i = 0; i < modelData.size(); i += 25) {
                int end = Math.min(i + 25, modelData.size());
                this.batchSave(modelData.subList(i, end));
            }
        } catch (IOException e) {
            log.error("读取表格数据异常: ", e);
            throw new IException("导入表格数据失败");
        }
    }

    public void exportExcel(String fileName, List<Map<String, Object>> data, HttpServletResponse response) {
        if (StringUtil.isEmpty(fileName)) {
            throw new IException("请指定文件名");
        }
        try {
            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
            if (StringUtil.isEmpty(fileName)) {
                fileName = this.getModelClazz().getAnnotation(Data.class).title();
            }
            if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
                fileName += ".xlsx";
            }
            List<Field> modelBaseFields = this.getModelBaseFields();
            List<String> dataKeyList = modelBaseFields.stream().map(Field::getName).toList();
            List<String> dataTitleList = modelBaseFields.stream().map(item -> {
                String title = item.getAnnotation(Column.class).title();
                if (StringUtil.isEmpty(title)) {
                    return item.getName();
                }
                return title;
            }).toList();
            ExcelUtil.writeOneSheetExcel(data, dataKeyList, dataTitleList, null, null, fileName, response);
        } catch (IOException e) {
            log.error("异常: ", e);
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            throw new IException("Excel导出失败", e);
        }
    }

    @SuppressWarnings("unchecked")
    public Class<T> getModelClazz() {
        return (Class<T>) CacheTool.getServiceModelGeneric(this.getClass());
    }

    @SuppressWarnings("unchecked")
    public Class<M> getMapperClazz() {
        return (Class<M>) CacheTool.getServiceMapperGeneric(this.getClass());
    }

    public List<Field> getModelFields() {
        return CacheTool.getServiceModelGenericFields(this.getClass());
    }

    public List<Field> getModelBaseFields() {
        return CacheTool.getServiceModelGenericBaseFields(this.getClass());
    }

    public Map<String, Field> getModelMapBaseFields() {
        return this.getModelBaseFields().stream().collect(Collectors.toMap(Field::getName, value -> value));
    }

    public Field getModelPrimaryField() {
        return CacheTool.getModelPrimaryField(this.getModelClazz());
    }

    public Map<String, Object> keyValuesArrayParamsToMap(@NotNull Object... keyValuesArray) {
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