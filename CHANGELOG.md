# mybatis-xml-fast-develop changelog

+ ## 1.6.0

已推送

隔了一周才推送，电脑主板进水了...

getId 方法默认使用 UUIDV7

解放掉 @Model 注解只能作用于表实体上的限制，方便扩展需求，相较于以前版本，略有破坏性更新

controller 多种 SoftDelete 模板方法

service 添加_delete(boolean confirm) 方法

解放模型包名限制，多包生成

+ ## 1.5.3

已推送

文档网站 github pages

mariadb 数据库生成和建表支持

damneg（达梦）数据库生成和建表支持

fix: postgresql 软删除参数问题

pageIndex，pageSize 参数类型修改

+ ## 1.5.2

已推送

添加 Primary 注解，从 Column 注解分离，显式指定主键是否自增，不再通过类型判断是否自增，以及相关逻辑改动

关于 id 的生成，不再强制使用，移除 ulid-creator 依赖，自行覆写选择性使用

postgresql 保存时自增主键问题，批量和 oracle、mssql 情况一样，警告信息，建议业务避免

+ ## 1.5.1

已推送

DeleteFlag 删除标识，相关软删除兼容

BaseModelWithDefaultFields 基类模型，附带常用默认字段，泛型主键

添加 like 条件生成处理，原有 instr 方法保留

com.wangshu.base.controller 内所有默认 @PostMapping 更改为 @RequestMapping，注意生产环境权限问题

BaseService getUUID 方法标记为 Deprecated，添加 getUlId 替代使用

fix：_update(P id, @NotNull String column1, Object newValue) 方法，参数处理错误

BaseDataService 添加 Query、这 O、那 O 的相关支持。。。

@NotNull List<T> _getNestList(@NotNull Object... keyValuesArray) 方法支持类似 _getNestList(KV(ArticleQuery::getTitleLike, titleLike)) 这样的写法。。。

+ ## 1.5.0

已推送 maven

DataBaseType postgresql、oracle、mssql xml 生成、自动建表等相关支持

createdAt，deletedAt，updatedAt，DefaultOrder 注解标注关键字段

基于 DeletedAt 注解标识的 _softDelete 软删除

SqlStyle Snake Upper 风格支持

+ ## 1.4.0

已推送 maven

修复自 1.2.0 编译时生成，scanClassFile = true 时（扫描已编译 model 模式）生成额外的 model bug

添加编译期生成强制覆盖 xml 配置

GenerateConfig 拆分为 GenerateConfig、GenerateConfigCompileTime

GenerateJava GenerateXml 方法修改、逻辑精简

generate-compile-time 同步 generate 模块修改，生成逻辑精简

添加多模块分层开发使用案例 mybatis-xml-fast-develop-multiple-module-example

service 中所有数据库相关操作方法名和 mapper 同步为 _ 开头作为区分

BaseDataService 添加 _saveUnCheckExist，保存但不校验是否真实存在

+ ## 1.3.0

已推送 maven

添加使用示例模块 mybatis-xml-fast-develop-example

优化对多查询映射

+ ## 1.2.0

已推送 maven

引入编译期生成功能

添加编译期生成配置 GenerateConfig

添加编译期强制触发注解 @ForceTrigger

优化多模块分层开发体验，编译期生成逻辑优化

+ ## 1.1.0

已推送 maven

Query 支持，emm...

+ ## 1.0.0

已推送 maven