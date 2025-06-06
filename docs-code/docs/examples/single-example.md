---
sidebar_position: 1
---

# 单体应用

mybatis-xml-fast-develop 单体应用案例

https://github.com/wangshu-g/mybatis-xml-fast-develop/tree/master/mybatis-xml-fast-develop-example/mybatis-xml-fast-develop-single-example

## 下载使用案例

```shell
git clone https://github.com/wangshu-g/mybatis-xml-fast-develop

# 单体应用
cd mybatis-xml-fast-develop/mybatis-xml-fast-develop-example/mybatis-xml-fast-develop-single-example/
```

## 创建数据库

表会根据模型类配置自动创建

```shell
mysql -u root -p

# Enter password: root

use mysql;

create database test;

show databases;

exit;
```

## 启动

### 通过 IDEA 启动

启动类

com.test.ApplicationSingleExample

```java
package com.test;

import com.wangshu.annotation.EnableConfig;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
// 自动建表，信息缓存
@EnableConfig(modelPackage = {"com.test.model"}, enableAutoInitTable = true)
@MapperScan(value = {"com.test.mapper"})
@SpringBootApplication
public class ApplicationSingleExample implements CommandLineRunner {

    @Override
    public void run(String... args) {
//        log.info("args: {}", Arrays.asList(args));
    }

    public static void main(String[] args) {
        SpringApplication.run(ApplicationSingleExample.class);
    }

}
```

### 通过命令行构建启动

```sh
mvn clean package

cd ./mybatis-xml-fast-develop-single-example/target/

java -jar -Dspring.profiles.active=dev ./mybatis-xml-fast-develop-single-example-1.0-SNAPSHOT.jar
```

