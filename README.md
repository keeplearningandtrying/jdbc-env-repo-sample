# Spring Cloud: JDBCEnvironmentRepository Sample Application
Sample of [Spring Cloud Config Server][2] embedded application that uses database as backend for configuration properties.

## Background
[Spring Cloud Config Server][2] provides several options to store configuration for an application. In general it is handled
by [Environment Repository][3]. 

Available options are git, file system, vault, svn, and database. This application demonstrates usage of [JdbcEnvironmentRepository][1] 
which allows an application to store its configuration in database.

## Configuration
In order to enable this feature we will include `spring-boot-starter-jdbc` as one of the dependencies for the application and
include `jdbc` as one of its active profiles.

### Include JDBC as dependency
This can be seen in [pom.xml][5].

```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
    </dependencies>
```

### Include `jdbc` as active profile
This can be seen in [bootstrap.yml][4].

```yaml
spring:
  profiles:
    active: jdbc
```

## Creating table and populating data
By default the [JdbcEnvironmentRepository][1] will look into a table called `PROPERTIES` which contains the following columns:

  - KEY
  - VALUE
  - APPLICATION
  - PROFILE
  - LABEL

### Create table schema
Schema to create the table can found in [schema-jdbc.sql][6]:

```sql
CREATE TABLE IF NOT EXISTS PROPERTIES (
  KEY         VARCHAR(2048),
  VALUE       VARCHAR(4096),
  APPLICATION VARCHAR(128),
  PROFILE     VARCHAR(128),
  LABEL       VARCHAR(128),
  PRIMARY KEY (`KEY`, `APPLICATION`, `PROFILE`, `LABEL`)
);
```

In the script above KEY, APPLICATION, PROFILE, and LABEL are marked as composite key in order to avoid duplicated entry.

### Populate table
For this demonstration will we have a configuration called `app.greet.name` and this will be populated upon start-up.
Its script can be found in [jdbc-data.sql][7].

```sql
INSERT INTO PROPERTIES (APPLICATION, PROFILE, LABEL, KEY, VALUE)
VALUES ('demo', 'default', 'master', 'app.greet.name', 'Demo');
```

The script above explains that the configuration `app.greet.name` belongs to:

  - an application called _demo_
  - with profile called _default_
  - and labelled _master_

## Configure Application Properties
In order for [JdbcEnvironmentRepository][1] to retrieve properties for this application it will need to be informed on
its name, profile, and label. This configurations can be found in [bootstrap.yml][4]

```yaml
spring:
  application:
    name: demo
  cloud:
    config:
      label: master
``` 

We are not configuring `spring.cloud.profile` because its default value is `default`.

## Create Bootstrap Application Context
Finally we will need to inform Spring Cloud on what are the classes needed in order to build the 
bootstrap application context. This can found in [spring.factories][8]:

```text
org.springframework.cloud.bootstrap.BootstrapConfiguration=\
  org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,\
  org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration
```

These two classes will help us to build [JdbcTemplate][10] which is needed to construct [JdbcEnvironmentRepository][9].

## Verify Configuration Properties
In order to ensure that the application will use configurations from database we will create same configuration in [application.yml][11]:

```yaml
app:
  greet:
    name: Default
```

We will have a [GreetService][12] which will retrieve the value for `app.greet.name`.

```java
@AllArgsConstructor
@Service
public class GreetService {

    private GreetProperties properties;

    public String greet(String greet) {
        return String.format("%s, my name is %s", greet, properties.getName());
    }

}
```

Next we will have [ApplicationTests][13] class that verifies that the value for `app.greet.name` is **Demo** and not **Default**:

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    @Autowired
    private GreetService service;

    @Test
    public void greetAndResponseContainsNameFromDatabase() {
        String response = service.greet("Hello");

        assertThat(response)
                .doesNotContain("Default")
                .contains("Demo");
    }
}
```

By executing `greetAndResponseContainsNameFromDatabase()` we verify that the returned response:

  - Does not contain the word **Default**
  - Contains the word **Demo**

[1]: https://cloud.spring.io/spring-cloud-config/single/spring-cloud-config.html#_jdbc_backend
[2]: https://cloud.spring.io/spring-cloud-config/single/spring-cloud-config.html#_spring_cloud_config_server
[3]: https://cloud.spring.io/spring-cloud-config/single/spring-cloud-config.html#_environment_repository
[4]: src/main/resources/bootstrap.yml
[5]: pom.xml
[6]: src/main/resources/schema-jdbc.sql
[7]: src/main/resources/data-jdbc.sql
[8]: src/main/resources/META-INF/spring.factories
[9]: https://github.com/spring-cloud/spring-cloud-config/blob/master/spring-cloud-config-server/src/main/java/org/springframework/cloud/config/server/environment/JdbcEnvironmentRepository.java
[10]: https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/jdbc/core/JdbcTemplate.html
[11]: src/main/resources/application.yml
[12]: src/main/java/rz/demo/jdbc/repo/greet/GreetService.java
[13]: src/test/java/rz/demo/jdbc/repo/ApplicationTests.java