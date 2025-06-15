---
sidebar_position: 1
---

# Maven pom.xml 配置

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