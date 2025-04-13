FROM wangshu10086/common-developer-images:openjdk-21-slim-font-support
LABEL authors="wangshu"

WORKDIR /app

ADD target/mybatis-xml-fast-develop-single-example-1.0-SNAPSHOT.jar .

ENTRYPOINT ["java","-jar","-Dspring.profiles.active=docker", "/app/mybatis-xml-fast-develop-single-example-1.0-SNAPSHOT.jar"]