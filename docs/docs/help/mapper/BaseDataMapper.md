# BaseDataMapper 常用方法

完全基于 xml，与生成的 xml 中方法一一对应，没什么好说的

```java
public interface BaseDataMapper<T extends BaseModel> extends BaseMapper {

    /**
     * <p>保存</p>
     *
     * @param model 保存参数,继承 {@link BaseModel}
     * @return int 影响行数
     **/
    int _save(T model);

    /**
     * <p>批量保存</p>
     *
     * @param modelList 保存参数,继承 {@link BaseModel}
     * @return int 影响行数
     **/
    int _batchSave(List<T> modelList);

    /**
     * <p>删除</p>
     *
     * @param map 删除条件
     * @return int 影响行数
     **/
    int _delete(Map<String, Object> map);

    /**
     * <p>更新</p>
     *
     * @param map 更新参数
     * @return int 影响行数
     **/
    int _update(Map<String, Object> map);

    /**
     * <p>查询一条</p>
     *
     * @param map 查询条件
     * @return T extends {@link BaseModel}
     **/
    T _select(Map<String, Object> map);

    /**
     * <p>查询列表</p>
     *
     * @param map 查询条件
     * @return List Map String Object
     **/
    List<Map<String, Object>> _getList(Map<String, Object> map);

    /**
     * <p>查询列表</p>
     *
     * @param map 查询条件
     * @return List T extends BaseModel
     **/
    List<T> _getNestList(Map<String, Object> map);

    /**
     * <p>数据条数</p>
     *
     * @param map 查询参数
     * @return int 数据条数
     **/
    int _getTotal(Map<String, Object> map);

}
```
