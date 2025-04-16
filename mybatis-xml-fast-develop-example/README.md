# mybatis-xml-fast-develop-example

## 简介

两个最佳使用案例，在生产环境都有实践

## mybatis-xml-fast-develop-single-example

这是一个 mybatis-xml-fast-develop 在单体项目的最佳实践

建好数据库后，直接启动/打包就可以，先最大化拉满体验试下

``` sh

mysql -u root -p 

# Enter password: root

use mysql;

create database test;

show databases;

exit;

git clone https://github.com/wangshu-g/mybatis-xml-fast-develop

cd mybatis-xml-fast-develop/mybatis-xml-fast-develop-example

mvn clean package

cd ./mybatis-xml-fast-develop-single-example/target/

java -jar -Dspring.profiles.active=dev ./mybatis-xml-fast-develop-single-example-1.0-SNAPSHOT.jar

# 默认全是 POST 自己覆写即可
curl -X POST http://localhost:8080/Article/getNestList

```

## mybatis-xml-fast-develop-multiple-module-example

这是一个 mybatis-xml-fast-develop 在多模块项目的最佳实践

单体模块分层架构、分布式模块分层架构都可套用

注意这个模块是伪代码，不可以运行

+ module1
+ module2
+ public-module
    + data-service 内部数据操作，可以在这里统一数据清洗、校验、缓存
    + dubbo-api dubbo 共享服务接口
    + model 模型
