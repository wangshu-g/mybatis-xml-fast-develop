package com.wangshu.base.controller.daoru;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.data.CellData;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.wangshu.base.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@EqualsAndHashCode(callSuper = false)
@Data
@Slf4j
public class ModelDataListener extends AnalysisEventListener<Object> {

    List<Map<String, Object>> dataList = new ArrayList<>();
    List<String> headList = new ArrayList<>();

    public <T extends BaseModel> List<T> mapToModelData(Class<T> modelClazz) {
        List<T> modelData = new ArrayList<>();
        try {
            for (Map<String, Object> rowData : this.dataList) {
                T model = modelClazz.getDeclaredConstructor().newInstance();
                model.setModelValuesFromMapByFieldNameWithTitle(rowData);
                modelData.add(model);
            }
        } catch (NoSuchMethodException | InstantiationException | InvocationTargetException |
                 IllegalAccessException e) {
            log.error("获取实例失败", e);
        }
        return modelData;
    }

    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        super.invokeHead(headMap, context);
        headList = headMap.values().stream().map(CellData::getStringValue).toList();
    }

    @Override
    public void invoke(Object rowData, AnalysisContext context) {
        if (rowData instanceof LinkedHashMap<?, ?> rowDataMap) {
            Map<String, Object> row = new HashMap<>();
            List<?> list = rowDataMap.values().stream().toList();
            for (int index = 0; index < list.size(); index++) {
                row.put(headList.get(index), list.get(index));
            }
            dataList.add(row);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }

}