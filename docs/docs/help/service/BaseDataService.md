---
sidebar_position: 1
---

# BaseDataService 常用方法

扩展自 mapper 中 xml 的方法，所有数据操作方法都以 _ 开头，作为区分使用

```java
public interface BaseDataService<P, T extends BaseModel> extends BaseService {

    // 保存，如果主键不为空，会根据主键查询是否存在，存在会转更新
    // 在 saveParamFilter 方法中会维护 @CreatedAt、@UpdatedAt、@DeleteFlag 标识字段的值
    int _save(@NotNull T model);

    int _save(@NotNull Map<String, Object> map);

    // 保存，如果主键不为空，会直接转更新，不会去查询是否真实存在 
    int _saveUnCheckExist(T model);

    int _saveUnCheckExist(@NotNull Map<String, Object> map);

    // 批量保存，注意 oracle、mssql、postgresql 的批量存在主键回写异常的情况，建议添加业务ID
    // oracle、mssql、postgresql 使用该方法会有警告信息
    int _batchSave(@NotNull List<T> modelList);

    // 删除，默认会有参数合法性校验避免意外的全表删除，若不存在有效删除参数，会抛出异常
    // 如果需要全表删除，请单独写个不验证有效参数的方法
    // 注意此处的删除是真实删除
    int _delete(@NotNull Map<String, Object> map);

    int _delete(@NotNull Object... keyValuesArray);

    int _delete(@NotNull T model);

    int _delete(P id);

    // 更新，默认会有参数合法性校验意外的全表更新，若不存在有效更新参数，会抛出异常
    // 在 updateParamFilter 方法中会维护 @UpdatedAt 标识字段的值
    int _update(@NotNull Map<String, Object> map);

    int _update(P id, String column1, Object newValue);

    // 使用该方法时，仅会根据主键更新，若主键不存在或是空值，会抛出异常
    int _update(@NotNull T model);

    int _update(@NotNull Object... keyValuesArray);

    // 软删除，会根据 @DeletedAt 或者 @DeleteFlag 标识的字段做软删除，都不存在会抛出异常
    // 在 softDeleteParamFilter 方法中会维护 @DeletedAt 或者 @DeleteFlag 标识的字段
    int _softDelete(@NotNull Map<String, Object> map);

    int _softDelete(P id);

    int _softDelete(@NotNull T model);

    int _softDelete(@NotNull Object... keyValuesArray);

    // 查询但条数据，若返回多条，会抛出异常
    // 会验证是否存在 @DeleteFlag 标识的字段，若该参数为空，会添加 deleteFlag = '0' 查询未删除数据
    @Nullable T _select(@NotNull Map<String, Object> map);

    @Nullable T _select(@NotNull T model);

    @Nullable T _select(@NotNull CommonQueryParam<T> query);

    @Nullable T _select(P id);

    @Nullable T _select(String column1, Object param1, String column2, Object param2);

    @Nullable T _select(@NotNull Object... keyValuesArray);

    // 查询连表列表，返回 Map 扁平化数据（没有 ResultMap 映射），会校验分页参数
    // 会验证是否存在 @DeleteFlag 标识的字段，若该参数为空，会添加 deleteFlag = '0' 查询未删除数据
    @NotNull List<Map<String, Object>> _getList(@NotNull Map<String, Object> map);

    @NotNull List<Map<String, Object>> _getList(@NotNull T model);

    @NotNull List<Map<String, Object>> _getList(@NotNull CommonQueryParam<T> query);

    @NotNull List<Map<String, Object>> _getList(String column, Object value);

    @NotNull List<Map<String, Object>> _getList(@NotNull Object... keyValuesArray);

    // 查询连表列表，返回 Map 扁平化数据（没有 ResultMap 映射），不校验分页参数
    // 会验证是否存在 @DeleteFlag 标识的字段，若该参数为空，会添加 deleteFlag = '0' 查询未删除数据
    @NotNull List<Map<String, Object>> _getListWithOutLimit(@NotNull Map<String, Object> map);

    @NotNull List<Map<String, Object>> _getListWithOutLimit(String column, Object value);

    @NotNull List<Map<String, Object>> _getListWithOutLimit(@NotNull T model);

    @NotNull List<Map<String, Object>> _getListWithOutLimit(@NotNull CommonQueryParam<T> query);

    @NotNull List<Map<String, Object>> _getListWithOutLimit(@NotNull Object... keyValuesArray);

    // 查询连表列表，返回 model 模型，会校验分页参数
    // 会验证是否存在 @DeleteFlag 标识的字段，若该参数为空，会添加 deleteFlag = '0' 查询未删除数据
    @NotNull List<T> _getNestList(@NotNull Map<String, Object> map);

    @NotNull List<T> _getNestList(@NotNull T model);

    @NotNull List<T> _getNestList(@NotNull CommonQueryParam<T> query);

    @NotNull List<T> _getNestList(String column, Object value);

    @NotNull List<T> _getNestList(@NotNull Object... keyValuesArray);
    
    // 查询连表列表，返回 model 模型，不校验分页参数
    // 会验证是否存在 @DeleteFlag 标识的字段，若该参数为空，会添加 deleteFlag = '0' 查询未删除数据
    @NotNull List<T> _getNestListWithOutLimit(@NotNull Map<String, Object> map);

    @NotNull List<T> _getNestListWithOutLimit(String column, Object value);

    @NotNull List<T> _getNestListWithOutLimit(@NotNull T model);

    @NotNull List<T> _getNestListWithOutLimit(@NotNull CommonQueryParam<T> query);

    @NotNull List<T> _getNestListWithOutLimit(@NotNull Object... keyValuesArray);

    // 返回符合条件的数据条数
    // 会验证是否存在 @DeleteFlag 标识的字段，若该参数为空，会添加 deleteFlag = '0' 查询未删除数据
    int _getTotal(@NotNull Map<String, Object> map);

    int _getTotal(@NotNull T model);

    int _getTotal(@NotNull CommonQueryParam<T> query);

    int _getTotal(String column, Object value);

    int _getTotal(@NotNull Object... keyValuesArray);

    int _getTotal();
    
}
```