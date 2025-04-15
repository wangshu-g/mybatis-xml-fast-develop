# mybatis-xml-fast-develop-example

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