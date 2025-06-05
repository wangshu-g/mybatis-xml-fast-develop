---
sidebar_position: 3
---

# 生成配置

位于 resources/application.yml 中，配置生成规则

## 默认值

```java
public class GenerateConfig {

    boolean model = false;
    boolean xml = true;
    boolean mapper = true;
    boolean service = true;
    boolean serviceImpl = true;
    boolean controller = true;
    boolean query = true;
    boolean scanClassFile = false;
    String scanClassFileModelPackage;
    boolean forceOverwriteXml = false;

}
```

| 配置项                                                 | 说明                               | 默认值   |
|-----------------------------------------------------|----------------------------------|-------|
| generate-compile-time.scan-class-file               | 配合 @ForceTrigger 使用，扫描已编译模型实体类   | false |
| generate-compile-time.scan-class-file-model-package | 配合 @ForceTrigger 使用，指定模型实体类全包名路径 | null  |
| generate-compile-time.force-cover-xml               | 强制覆盖旧的 XML 文件                    | false |
| generate-compile-time.model                         | 反向生成 model，暂时没用到                 | false |
| generate-compile-time.controller                    | 是否生成 controller                  | true  |
| generate-compile-time.mapper                        | 是否生成 mapper                      | true  |
| generate-compile-time.service                       | 是否生成 service                     | true  |
| generate-compile-time.service-impl                  | 是否生成 service-impl                | true  |
| generate-compile-time.query                         | 是否生成 query                       | true  |
| generate-compile-time.xml                           | 是否生成 XML                         | true  |

```yaml
mybatis-xml-fast-develop:
  generate-compile-time:
    # 配合 @ForceTrigger 使用，扫描已编译模型实体类
    scan-class-file: true
    # 配合 @ForceTrigger 使用，指定模型实体类全包名路径
    scan-class-file-model-package: com.module2.model
    # 强制覆盖旧的 xml 文件
    force-cover-xml: true
    # 是否生成 controller
    controller: false
    # 是否生成 mapper
    mapper: true
    # 是否生成 service
    service: true
    # 是否生成 service-impl
    service-impl: true
    # 是否生成 query
    query: true
    # 是否生成 xml
    xml: true
```