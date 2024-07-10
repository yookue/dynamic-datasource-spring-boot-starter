# Dynamic Datasource Spring Boot Starter

Spring Boot application integrates dynamic `DataSource` quickly.

## Quickstart

- Import dependencies

```xml
    <dependency>
        <groupId>com.yookue.springstarter</groupId>
        <artifactId>dynamic-datasource-spring-boot-starter</artifactId>
        <version>LATEST</version>
    </dependency>
```

> By default, this starter will auto take effect, you can turn it off by `spring.dynamic-datasource.enabled = false`

- Configure Spring Boot `application.yml` with prefix `spring.dynamic-datasource`

```yml
spring:
    dynamic-datasource:
        datasource-route:
            testdb1:
                url: 'jdbc:mysql://127.0.0.1:3306/test_db1'
                driver-class-name: com.mysql.cj.jdbc.Driver
                type: com.zaxxer.hikari.HikariDataSource
            testdb2:
                url: 'jdbc:mysql://127.0.0.1:3306/test_db2'
                driver-class-name: com.mysql.cj.jdbc.Driver
                type: com.mchange.v2.c3p0.ComboPooledDataSource
            testdb3:
                url: 'jdbc:mysql://127.0.0.1:3306/test_db3'
                driver-class-name: com.mysql.cj.jdbc.Driver
                type: org.apache.commons.dbcp2.BasicDataSource
```

> The identifiers (`testdb1`, `testdb2`, `testdb3`) are used as keys for switching several `DataSource`, they could be any names if you like, just attention that, these names must be alphanumeric.

- Annotate your (non-static)  method with `@DataSourceRouting` annotation, done!
Take one of the previous database `testdb1` for example, the annotation is
```
    @DataSourceRouting(key = "testdb1")
```

> Note that the `key` segment, that is the database identifier under your `datasource-route` node of the previous step.

- **Optional feature**: You can also define your databases in code (not just the `application.properties`), what you have to do is declaring `DataSource` beans with the `@DataSourceDefinition` annotation.

- This starter supports the most popular data source pools in the word, including
  - c3p0
  - dbcp2
  - druid
  - hikari
  - oracle ucp
  - tomcat
  - vibur

## Document

- Github: https://github.com/yookue/dynamic-datasource-spring-boot-starter

## Requirement

- jdk 17+

## License

This project is under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0)

See the `NOTICE.txt` file for required notices and attributions.

## Donation

You like this package? Then [donate to Yookue](https://yookue.com/public/donate) to support the development.

## Website

- Yookue: https://yookue.com
