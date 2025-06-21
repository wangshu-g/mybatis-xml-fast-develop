---
sidebar_position: 15
---

# @EnableConfig

启用 mybatis-xml-fast-develop-starter 配置

自动建表，自定义异常，提前初始化缓存元数据

| 属性名称                  | 注释                 |
|-----------------------|--------------------|
| modelPackage          | 模型包路径              |
| enableAutoInitTable   | 默认关闭，生产环境上线建议关闭    |
| targetDataSource      | 目标数据源，默认是 {"*"} 所有 |
| enableExceptionHandle | 是否启用异常处理，默认启用      |

```java
@EnableConfig(modelPackage = {"com.test.model"}, enableAutoInitTable = true)
public class ApplicationTest {

}
```