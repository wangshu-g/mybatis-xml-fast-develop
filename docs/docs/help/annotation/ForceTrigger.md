---
sidebar_position: 1
---

# @ForceTrigger

强制触发钩子，多模块分层开发可能会用到

作为 mybatis-xml-fast-develop-generate-compile-time 中强制触发扫描已编译（即其他模块已编译模型类）的钩子

如果该注解存在于模块中，编译期会根据生成配置项中的 scan-class-file-model-package 属性扫描模型实体类

配置详情见 [配置文档](/docs/config/generate-config)

```java
// 放在需要触发的模块任意位置，编译期读取模块下的生成配置项
@ForceTrigger
public class Trigger {
    
}
```