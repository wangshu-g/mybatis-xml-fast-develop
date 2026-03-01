## 🆕 1.6.3

> ✅ 已推送

### 依赖变更

* 移除 hutool 工具包，使用 commons、spring 等内置工具包替代
* 父依赖变更至 spring-boot-starter-parent 4.0.0

## 🆕 1.6.2

> ✅ 已推送至 Maven Central

### 🐞 修复

* T _select(P id) 修改，若 id 为空字符串直接返回 null
* alterTable 方法逻辑调整，修复 GenerateTableMysql 获取错误数据库表信息引起的列类型变更错误、新增列错误

## 🆕 1.6.1

> ✅ 已推送至 Maven Central

### 🐞 修复

* 修复 **子模型元数据未正确处理 `dataBaseType` 导致生成错误的 XML 文件**
  [#2](https://github.com/wangshu-g/mybatis-xml-fast-develop/issues/2)
* 修复 **保存前根据主键查询时未忽略删除标记字段** 的问题

### ✨ 新增

* 新增 `@Version` 注解 —— 支持 **乐观锁版本控制**
* 新增 `@DefaultValue` 注解 —— 支持 **字段默认值定义**
* 支持基于 `@Version` 注解的 **乐观更新、自增、自减**

---

## 🆕 1.6.0

> ✅ 已推送至 Maven Central
> 💬 注：隔了一周才推送（主板进水 ☠️）

### ✨ 新增

* `getId()` 方法默认使用 **UUIDv7**
* 添加 `controller` 多种 **SoftDelete** 模板方法
* `service` 添加 `_delete(boolean confirm)` 方法
* 支持 **多包模型生成**，解除包名限制

### 🚨 破坏性更新

* `@Model` 注解不再仅限于表实体类，可作用于更多扩展场景（略有兼容性影响）

---

## 🆕 1.5.3

> ✅ 已推送至 Maven Central

### ✨ 新增

* 发布 **文档网站 (GitHub Pages)**
* 新增数据库支持：

    * ✅ **MariaDB**
    * ✅ **达梦（Dameng）**
* 新增多数据库自动建表支持

### 🐞 修复

* 修复 PostgreSQL **软删除参数问题**
* 修改 `pageIndex`、`pageSize` 参数类型以增强兼容性

---

## 🆕 1.5.2

> ✅ 已推送至 Maven Central

### ✨ 新增

* 添加 `@Primary` 注解（从 `@Column` 分离），显式指定主键自增策略
* 移除强制 ID 生成逻辑（移除 `ulid-creator` 依赖，可自定义 ID 策略）

### 🐞 修复 / 优化

* PostgreSQL 保存自增主键时的兼容性提示优化（与 Oracle / MSSQL 一致）

  > 建议业务层避免批量自增场景

---

## 🆕 1.5.1

> ✅ 已推送至 Maven Central

### ✨ 新增

* 新增软删除标识 `@DeleteFlag` 及相关兼容支持
* 新增 `BaseModelWithDefaultFields` 基类（带默认通用字段）
* 新增 `@Like` 条件生成支持（保留原 `instr` 方法）
* 新增 `getUlId()` 方法替代过时的 `getUUID()`

### ⚙️ 改进

* 所有默认 `@PostMapping` 改为 `@RequestMapping`
* `_getNestList()` 支持基于 KV 的多条件查询写法（示例：`KV(ArticleQuery::getTitleLike, titleLike)`）

### 🐞 修复

* 修复 `_update(P id, @NotNull String column1, Object newValue)` 参数处理错误

---

## 🆕 1.5.0

> ✅ 已推送至 Maven Central

### ✨ 新增

* 支持以下数据库自动建表与 XML 生成：

    * **PostgreSQL**
    * **Oracle**
    * **Microsoft SQL Server**
* 新增关键字段注解：

    * `@CreatedAt`、`@DeletedAt`、`@UpdatedAt`、`@DefaultOrder`
* 新增基于 `@DeletedAt` 的软删除 `_softDelete`
* 新增 SQL 风格：`SqlStyle.SnakeUpper`

---

## 🆕 1.4.0

> ✅ 已推送至 Maven Central

### 🐞 修复

* 修复编译期生成时（`scanClassFile = true`）会生成额外 model 的问题

### ✨ 新增

* 添加 **编译期强制覆盖 XML 配置**
* 拆分 `GenerateConfig` → `GenerateConfig` 与 `GenerateConfigCompileTime`
* 添加 **多模块分层开发案例**：`mybatis-xml-fast-develop-multiple-module-example`

### ⚙️ 优化

* 简化 `GenerateJava` / `GenerateXml` 方法逻辑
* 统一 service 层数据库操作命名为 `_` 前缀区分
* 新增 `_saveUnCheckExist()` —— 保存但不校验存在性

---

## 🆕 1.3.0

> ✅ 已推送至 Maven Central

### ✨ 新增

* 新增使用示例模块 `mybatis-xml-fast-develop-example`

### ⚙️ 优化

* 优化多查询映射逻辑

---

## 🆕 1.2.0

> ✅ 已推送至 Maven Central

### ✨ 新增

* 引入 **编译期生成机制**
* 新增 `GenerateConfig` 编译期配置
* 新增编译期强制触发注解 `@ForceTrigger`

### ⚙️ 优化

* 优化多模块分层开发体验与编译期逻辑

---

## 🆕 1.1.0

> ✅ 已推送至 Maven Central

### ✨ 新增

* 添加 `Query` 支持

---

## 🆕 1.0.0

> ✅ 已推送至 Maven Central

### 🎉 初始版本

* 核心功能发布
