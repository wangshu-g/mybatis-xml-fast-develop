# mybatis-xml-fast-develop

## 介绍

一个 mybatis-xml、spring boot 快速开发集合、mybatis-xml 编译期类似 lombok 的插件。

它不是为了完全避免手写 xml，而是最大化发挥、保留 mybatis-xml 直观、结构体映射的优势

不需要再手动维护普通的联表、结构体映射等，像 lombok 一样不再手动维护 get、set、equal 等方法一样的效果

引入依赖、注解后，仅需实体，无需其他代码，直接一键启动

oracle、mssql、postgresql、mysql 语法已作支持，支持以上数据库协议可以先使用已有配置去生成、建表

受限于精力，一些未支持

了解相关数据库可以自行继承、覆写方法实现

欢迎一起开发适配

## 视频使用演示

https://www.bilibili.com/video/BV17t5kzNEU2

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

## 为什么是 mybatis-xml

**直观，不存在黑盒，所见即所得**

完全基于 xml ，对于团队和开发人员，它可以是 **零心智负担、侵入**

你也可以通过配置仅使用 xml 产物

## 如何使用

注意：请使用 maven 不再使用 github packages（后续将会清理掉）

maven 仓库链接:

https://central.sonatype.com/artifact/io.github.wangshu-g/mybatis-xml-fast-develop

```xml

<dependencys>

    <dependency>
        <groupId>io.github.wangshu-g</groupId>
        <artifactId>mybatis-xml-fast-develop-core</artifactId>
        <version>1.5.2</version>
    </dependency>

    <dependency>
        <groupId>io.github.wangshu-g</groupId>
        <artifactId>mybatis-xml-fast-develop-starter</artifactId>
        <version>1.5.2</version>
    </dependency>

  <!--这个引入 mybatis-xml-fast-develop-generate-compile-time 就不需要自己手动调用生成了-->
    <dependency>
        <groupId>io.github.wangshu-g</groupId>
        <artifactId>mybatis-xml-fast-develop-generate</artifactId>
        <version>1.5.2</version>
    </dependency>

    <dependency>
        <groupId>io.github.wangshu-g</groupId>
        <artifactId>mybatis-xml-fast-develop-generate-compile-time</artifactId>
        <version>1.5.2</version>
        <!--对于项目中存在某些动态编译场景，这里很重要哦！！！（编译不会引入该依赖）-->
        <scoppe>provided</scoppe>
    </dependency>

</dependencys>

```

## 联系方式

备注来意哦，欢迎一起开发适配

<center>
  <img alt="author-qq-qrcode.jpg" height="1158" src="author-qq-qrcode.jpg" width="588"/>
</center>
