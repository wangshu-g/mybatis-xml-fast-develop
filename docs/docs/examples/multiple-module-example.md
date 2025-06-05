---
sidebar_position: 2
---

# 多模块分层应用

mybatis-xml-fast-develop 多模块分层应用案例

https://github.com/wangshu-g/mybatis-xml-fast-develop/tree/master/mybatis-xml-fast-develop-example/mybatis-xml-fast-develop-multiple-module-example

注意这个模块是伪代码，不可以运行

单体模块分层、分布式多模块都可套用

+ module1
+ module2
+ public-module
    + data-service 内部数据服务，可以在这里统一数据清洗、校验、缓存
    + dubbo-api dubbo 共享服务接口
    + model 共用模型

## 下载使用案例

```sh
git clone https://github.com/wangshu-g/mybatis-xml-fast-develop

# 单体应用
cd mybatis-xml-fast-develop/mybatis-xml-fast-develop-example/mybatis-xml-fast-develop-multiple-module-example/
```

