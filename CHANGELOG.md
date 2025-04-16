# mybatis-xml-fast-develop changelog

+ ## 1.4.0-dev

service 中所有数据库相关操作方法名和 mapper 同步为 _ 开头作为区分

GenerateJava GenerateXml 方法修改、逻辑精简

generate-compile-time 同步 generate 模块修改，生成逻辑精简

添加多模块分层开发使用案例 mybatis-xml-fast-develop-multiple-module-example

BaseDataService 添加 _saveUnCheckExist，保存但不校验是否真实存在

GenerateConfig 拆分为 GenerateConfig、GenerateConfigCompileTime

修复自 1.2.0 编译时生成，scanClassFile = true 时（扫描已编译 model 模式）生成额外的 model bug

添加编译期生成强制覆盖 xml 配置

+ ## 1.3.0

已发布

添加使用示例模块 mybatis-xml-fast-develop-example

优化对多查询映射

+ ## 1.2.0

已发布

引入编译期生成功能

添加编译期生成配置 GenerateConfig

添加编译期强制触发注解 @ForceTrigger

优化多模块分层开发体验，编译期生成逻辑优化

+ ## 1.1.0

已发布

Query 支持，emm...

+ ## 1.0.0

已发布