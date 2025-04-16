# mybatis-xml-fast-develop

## 介绍

一个 mybatis-xml、spring boot 快速开发集合、插件。

引入依赖、注解后，仅需实体，无需其他代码，直接一键启动

完全可以支持任意数据库 xml 生成，但受限于精力，一些未支持

了解相关数据库可以自行继承、覆写方法实现

欢迎一起开发适配

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
    + 如果没有被触发，这是因为 IDEA 编辑器的编译策略导致，手动 Build --> Rebuild Module 'xxx'(Ctrl Shift F9)
+ mybatis-xml-fast-develop-example
    + 最佳实践案例
    + mybatis-xml-fast-develop-single-example 单体应用案例
    + mybatis-xml-fast-develop-multiple-module-example 多模块应用案例

## 为什么是 mybatis-xml

**直观**

对于团队和开发人员，它可以是 **零心智负担、侵入**

你也可以通过配置仅使用 xml 产物

## 如何使用

maven 仓库链接:

https://central.sonatype.com/artifact/io.github.wangshu-g/mybatis-xml-fast-develop

```xml

<dependencys>

    <dependency>
        <groupId>io.github.wangshu-g</groupId>
        <artifactId>mybatis-xml-fast-develop-core</artifactId>
        <version>1.3.0</version>
    </dependency>

    <dependency>
        <groupId>io.github.wangshu-g</groupId>
        <artifactId>mybatis-xml-fast-develop-starter</artifactId>
        <version>1.3.0</version>
    </dependency>

    <!--这个一般引入 mybatis-xml-fast-develop-generate-compile-time 就不需要自己调用生成了-->
    <dependency>
        <groupId>io.github.wangshu-g</groupId>
        <artifactId>mybatis-xml-fast-develop-generate</artifactId>
        <version>1.3.0</version>
    </dependency>

    <dependency>
        <groupId>io.github.wangshu-g</groupId>
        <artifactId>mybatis-xml-fast-develop-generate-compile-time</artifactId>
        <version>1.3.0</version>
        <!--对于项目中存在某些动态编译场景，这里很重要哦！！！（编译不会引入该依赖）-->
        <scoppe>provided</scoppe>
    </dependency>

</dependencys>

```

## 联系方式

备注来意哦

<center>
  <img alt="author-qq-qrcode.jpg" height="1158" src="author-qq-qrcode.jpg" width="588"/>
</center>