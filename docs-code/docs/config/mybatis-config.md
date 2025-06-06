---
sidebar_position: 2
---

# Mybatis 配置

对 mybatis 的配置没有任何侵入，注意配置下 mapper-locations 就好

```yaml
mybatis:
  configuration:
    cache-enabled: true
    map-underscore-to-camel-case: true
    call-setters-on-nulls: true
  mapper-locations:
    # 不会被覆盖，自定义 xml 注意单独找个文件夹
    - classpath:mapper/*.xml
    # 可选强制覆盖
    - classpath:[模块名称]/*.xml
```