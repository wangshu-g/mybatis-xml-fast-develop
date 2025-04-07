# mybatis-xml-fast-develop

## 介绍

一个 mybatis、springboot 快速开发集合。

完全可以支持任意数据库（受限于精力，一些未支持），欢迎一起开发适配，了解相关数据库可以自行继承、覆写方法实现

## 模块简介

+ mybatis-xml-fast-develop-core 基本功能，基类，字段、关联关系标识
+ mybatis-xml-fast-develop-starter 自动建表，异常控制
+ mybatis-xml-fast-develop-generate 代码生成模块
+ mybatis-xml-fast-develop-generate-compile-time 全自动，类似 lombok，编译期根据配置生成，根据包名自动拷贝至源代码处（不会覆盖同名文件），方便修改

## 为什么是 mybatis-xml

**直观**

## 使用

```xml

<dependencys>

    <dependency>
        <groupId>io.github.wangshu-g</groupId>
        <artifactId>mybatis-xml-fast-develop-core</artifactId>
        <version>1.1.0</version>
    </dependency>

    <dependency>
        <groupId>io.github.wangshu-g</groupId>
        <artifactId>mybatis-xml-fast-develop-starter</artifactId>
        <version>1.1.0</version>
    </dependency>

    <dependency>
        <groupId>io.github.wangshu-g</groupId>
        <artifactId>mybatis-xml-fast-develop-generate</artifactId>
        <version>1.1.0</version>
    </dependency>

    <dependency>
        <groupId>io.github.wangshu-g</groupId>
        <artifactId>mybatis-xml-fast-develop-generate-compile-time</artifactId>
        <version>1.1.0</version>
    </dependency>

</dependencys>

```