# mybatis-xml-fast-develop

## 介绍

一个 mybatis-xml、spring boot 快速开发集合、mybatis-xml 编译期类似 lombok 的插件。

用法类似 lombok，通过编译期生成模板 xml。

其完全基于 mybatis xml 扩展功能，因此引入后不会为团队带来任何学习成本和心智负担，几乎为零的侵入性，不存在任何黑盒

受限于精力，一些数据库语法生成暂未支持，欢迎一起开发适配

## 文档网站

https://wangshu-g.github.io/mybatis-xml-fast-develop-docs/

## 模块简介

+ mybatis-xml-fast-develop-core
    + 基本功能，基类、字段、注解关联关系标识
+ mybatis-xml-fast-develop-starter
    + 启动自动建表，异常控制
+ mybatis-xml-fast-develop-generate
    + 主要代码生成模块
+ mybatis-xml-fast-develop-generate-compile-time
    + 以上的是子弹，这个就是全自动步枪。
    + 类似 lombok，编译期根据配置生成，一键启动支持，还会根据包名将生成的源代码自动拷贝至源代码处（不会覆盖原同名文件），方便开发修改
    + 该组件解决使用 mybatis 及其相关组件进行开发时，sql、结构体映射等 xml 编写复杂耗时等问题，连表仅需在 model 基类字段添加注解即可
    + 如果没有被触发，这是因为 IDEA 编辑器的编译变更策略导致，手动 Build --> Rebuild Module 'xxx'(Ctrl Shift F9)
+ mybatis-xml-fast-develop-example
    + 最佳实践案例
    + mybatis-xml-fast-develop-single-example 单体应用案例
    + mybatis-xml-fast-develop-multiple-module-example 多模块应用案例

## 生成语法和自动建表数据库支持

| 枚举值                     | 数据库类型说明              | 
|-------------------------|----------------------|
| DataBaseType.oracle     | Oracle               |
| DataBaseType.mssql      | Microsoft SQL Server |
| DataBaseType.postgresql | Postgresql           |
| DataBaseType.mysql      | MySQL（默认）            |
| DataBaseType.mariadb    | MariaDB              |
| DataBaseType.dameng     | 达梦数据库（Dameng）        |

## 常用sql语法风格

| 枚举值          | 命名风格                        | 示例        |
|--------------|-----------------------------|-----------|
| SqlStyle.lcc | Lower Camel Case（默认，小写开头驼峰） | `userId`  |
| SqlStyle.sc  | Snake Case（小写蛇形）            | `user_id` |
| SqlStyle.su  | Snake Upper（大写蛇形）           | `USER_ID` |

## 视频使用演示

https://www.bilibili.com/video/BV17t5kzNEU2

## 如何使用

注意：请使用 maven 不再使用 github packages（后续将会清理掉）

maven 仓库链接:

https://central.sonatype.com/artifact/io.github.wangshu-g/mybatis-xml-fast-develop

```xml

<dependencys>

    <dependency>
        <groupId>io.github.wangshu-g</groupId>
        <artifactId>mybatis-xml-fast-develop-core</artifactId>
        <version>1.5.3</version>
    </dependency>

    <dependency>
        <groupId>io.github.wangshu-g</groupId>
        <artifactId>mybatis-xml-fast-develop-starter</artifactId>
        <version>1.5.3</version>
    </dependency>

    <!--这个引入 mybatis-xml-fast-develop-generate-compile-time 就不需要自己手动调用生成了-->
    <dependency>
        <groupId>io.github.wangshu-g</groupId>
        <artifactId>mybatis-xml-fast-develop-generate</artifactId>
        <version>1.5.3</version>
    </dependency>

    <dependency>
        <groupId>io.github.wangshu-g</groupId>
        <artifactId>mybatis-xml-fast-develop-generate-compile-time</artifactId>
        <version>1.5.3</version>
        <!--对于项目中存在某些动态编译场景，这里很重要哦！！！（编译不会引入该依赖）-->
        <scoppe>provided</scoppe>
    </dependency>

</dependencys>

```

## 联系方式

2560334673@qq.com

wangshu10086@gmail.com

表明来意哦，欢迎一起开发适配