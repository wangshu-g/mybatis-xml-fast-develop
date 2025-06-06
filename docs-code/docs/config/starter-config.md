---
sidebar_position: 4
---

# mybatis-xml-fast-develop-starter 配置

自动建表，自定义异常，提前初始化缓存元数据

通过在主类添加 @EnableConfig 注解启用，[详情见](/docs/help/annotation/EnableConfig)

```java
@Slf4j
@EnableConfig(modelPackage = {"com.test.model"}, enableAutoInitTable = true)
@MapperScan(value = {"com.test.mapper"})
@SpringBootApplication
public class ApplicationTest implements CommandLineRunner {

}
```