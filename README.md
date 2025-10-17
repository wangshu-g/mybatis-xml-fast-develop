# 🧩 mybatis-xml-fast-develop

## 📘 项目简介

mybatis-xml-fast-develop 是一个面向 Spring Boot + MyBatis 的快速开发框架插件，旨在通过编译期自动生成 MyBatis
XML、Mapper、ServiceInterface、ServiceInstance、Controller 等模板代码，从而大幅减少使用 Mybatis 时繁琐的工作。其设计目标是零侵入、零学习成本，不引入神秘黑盒逻辑，尤其对喜爱Mybatis
XML开发极其友好。

* 它完全基于 MyBatis XML 原生机制实现
* 几乎 **零学习成本**、**零心智负担**，对现有项目 **无侵入性**，**无黑盒逻辑**
* 自动在编译期根据实体、注解生成对应的 XML 映射与 SQL；
* 支持常见数据库（MySQL / PostgreSQL / Oracle / SQL Server / MariaDB / 达梦等）；
* 支持多种命名风格（如驼峰、下划线、上划线等）；
* 包括核心模块、自动建表、生成模块、编译期插件与示例工程等；
* 可作为 “Lombok + MyBatis XML 生成器” 插件使用，让开发者免于手写冗长的 XML。

---

## 🌐 官方文档

👉 [mybatis-xml-fast-develop-docs](https://wangshu-g.github.io/mybatis-xml-fast-develop-docs/)

---

## 🚀 快速使用

> ⚠️ 请使用 **Maven Central** 仓库，不再使用 GitHub Packages（后续将清理 GitHub Packages）。

Maven 仓库地址：
🔗 [https://central.sonatype.com/artifact/io.github.wangshu-g/mybatis-xml-fast-develop](https://central.sonatype.com/artifact/io.github.wangshu-g/mybatis-xml-fast-develop)

### 📦 Maven 依赖示例

```xml

<dependencies>

    <dependency>
        <groupId>io.github.wangshu-g</groupId>
        <artifactId>mybatis-xml-fast-develop-core</artifactId>
        <version>1.6.1</version>
    </dependency>

    <dependency>
        <groupId>io.github.wangshu-g</groupId>
        <artifactId>mybatis-xml-fast-develop-starter</artifactId>
        <version>1.6.1</version>
    </dependency>

    <!-- 引入后无需手动执行生成操作 -->
    <dependency>
        <groupId>io.github.wangshu-g</groupId>
        <artifactId>mybatis-xml-fast-develop-generate</artifactId>
        <version>1.6.1</version>
    </dependency>

    <!-- 编译期生成插件（类似 Lombok） -->
    <dependency>
        <groupId>io.github.wangshu-g</groupId>
        <artifactId>mybatis-xml-fast-develop-generate-compile-time</artifactId>
        <version>1.6.1</version>
        <!-- 对于存在动态编译场景的项目，该配置非常重要 -->
        <scope>provided</scope>
    </dependency>

</dependencies>
```

---

## 🗄️ 数据库支持与自动建表

> 当前版本已适配多种数据库类型（部分生成语法仍在完善中，欢迎参与适配与反馈）

| 枚举值                       | 数据库类型                | 测试镜像版本                                               | 精确版本                                         |
|---------------------------|----------------------|------------------------------------------------------|----------------------------------------------|
| `DataBaseType.oracle`     | Oracle               | `container-registry.oracle.com/database/free:latest` | Oracle Database 23ai Free Release 23.0.0.0.0 |
| `DataBaseType.mssql`      | Microsoft SQL Server | `mcr.microsoft.com/mssql/server:2019-latest`         | SQL Server 2019 (RTM-CU32-GDR, 15.0.4440.1)  |
| `DataBaseType.postgresql` | PostgreSQL           | `postgres:latest`                                    | PostgreSQL 17.6                              |
| `DataBaseType.mysql`      | MySQL（默认）            | `mysql:8`                                            | MySQL 8.4.6                                  |
| `DataBaseType.mariadb`    | MariaDB              | `mariadb:latest`                                     | MariaDB 12.0.2                               |
| `DataBaseType.dameng`     | 达梦数据库（Dameng）        | `dm8_single:dm8_20240715_rev232765_x86_rh6_64`       | DM8 v8                                       |

> 测试用例部分数据库尚未完全覆盖，如发现问题，欢迎反馈。

---

## 🧾 SQL 命名风格

| 枚举值            | 命名风格                      | 示例        |
|----------------|---------------------------|-----------|
| `SqlStyle.lcc` | Lower Camel Case（默认，小写驼峰） | `userId`  |
| `SqlStyle.sc`  | Snake Case（小写下划线）         | `user_id` |
| `SqlStyle.su`  | Snake Upper（大写下划线）        | `USER_ID` |

---

## 🧩 模块说明

| 模块                                                 | 功能说明                                                                                                              |
|----------------------------------------------------|-------------------------------------------------------------------------------------------------------------------|
| **mybatis-xml-fast-develop-core**                  | 基础功能：基类、字段定义、注解映射等核心逻辑                                                                                            |
| **mybatis-xml-fast-develop-starter**               | 自动建表、异常控制、应用启动支持                                                                                                  |
| **mybatis-xml-fast-develop-generate**              | 主体代码生成模块                                                                                                          |
| **mybatis-xml-fast-develop-generate-compile-time** | 编译期自动生成模块，类似 Lombok，可根据注解与配置在编译阶段自动生成 XML 与源代码（不会覆盖原文件）。若未触发生成，可执行 `Build → Rebuild Module (Ctrl+Shift+F9)` 重新编译。 |
| **mybatis-xml-fast-develop-example**               | 示例项目集合（包含单体与多模块示例）                                                                                                |

---

## 🎥 视频演示

📺 [点击查看使用演示（Bilibili）](https://www.bilibili.com/video/BV17t5kzNEU2)

---

## 📬 联系方式

* 📧 [2560334673@qq.com](mailto:2560334673@qq.com)
* 📧 [wangshu10086@gmail.com](mailto:wangshu10086@gmail.com)

> 请在邮件中注明来意，欢迎开发者共同参与适配与功能扩展！