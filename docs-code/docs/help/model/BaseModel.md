---
sidebar_position: 1
---

# BaseModel

model 模型基类，继承 EntityTool，附带一些常用的实体类方法

注意模型类需要继承该类，[详情见](/docs/struct/model)

```java
public class BaseModel extends EntityTool {

    @Override
    public List<Field> modelFields() {
        return CacheTool.getModelFields(this.getClass());
    }

    public @Nullable Field modelPrimaryField() {
        return CacheTool.getModelPrimaryField(this.getClass());
    }

    public void setModelValuesFromMapByFieldNameWithTitle(@NotNull Map<String, Object> map) {
        Map<String, Field> fields = this.modelFieldsMap();
        fields.forEach((fieldName, field) -> {
            String title = fieldName;
            Column annotation = field.getAnnotation(Column.class);
            if (Objects.nonNull(annotation) && StrUtil.isNotBlank(annotation.title())) {
                title = annotation.title();
            }
            if (Objects.nonNull(map.get(title))) {
                field.setAccessible(true);
                try {
                    field.set(this, Convert.convert(field.getType(), map.get(title)));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

}
```