# Dependencies — OpenMRS Core 2.0.3

## Internal Module Dependencies

```
tools (standalone — no OpenMRS dependencies)
   │
test (POM aggregator — test dependency collection)
   │   Uses: JUnit, Mockito, PowerMock, H2, Spring Test, DBUnit, XMLUnit, Hamcrest
   │
api (JAR — core domain + services + DAOs)
   │   Depends on: test (test scope)
   │   Depends on: Spring, Hibernate, Liquibase, HAPI, Jackson, Log4j, etc.
   │
web (JAR — web layer: filters, controllers, servlets)
   │   Depends on: api (compile scope)
   │   Depends on: test, api-test-jar (test scope)
   │   Additional: Servlet API, JSP, JSTL, Spring MVC, OWASP Encoder, commons-fileupload
   │
webapp (WAR — final deployable artifact)
       Depends on: api (compile), web (compile)
       Depends on: test (test scope)
       Additional: Jetty plugin, Tomcat plugin
```

## External Dependencies — Runtime/Production

### Core Frameworks (HIGH severity — EOL/deprecated)

| Dependency | Group ID | Version | Current Version | Status |
|-----------|----------|---------|-----------------|--------|
| Spring Core | org.springframework:spring-core | 4.1.4.RELEASE | 6.x | **EOL** (Dec 2020) |
| Spring Beans | org.springframework:spring-beans | 4.1.4.RELEASE | 6.x | EOL |
| Spring Context | org.springframework:spring-context | 4.1.4.RELEASE | 6.x | EOL |
| Spring AOP | org.springframework:spring-aop | 4.1.4.RELEASE | 6.x | EOL |
| Spring ORM | org.springframework:spring-orm | 4.1.4.RELEASE | 6.x | EOL |
| Spring TX | org.springframework:spring-tx | 4.1.4.RELEASE | 6.x | EOL |
| Spring JDBC | org.springframework:spring-jdbc | 4.1.4.RELEASE | 6.x | EOL |
| Spring Web | org.springframework:spring-web | 4.1.4.RELEASE | 6.x | EOL |
| Spring WebMVC | org.springframework:spring-webmvc | 4.1.4.RELEASE | 6.x | EOL |
| Spring OXM | org.springframework:spring-oxm | 4.1.4.RELEASE | 6.x | EOL |
| Hibernate Core | org.hibernate:hibernate-core | 4.3.9.Final | 6.x | EOL |
| Hibernate C3P0 | org.hibernate:hibernate-c3p0 | 4.3.9.Final | 6.x | EOL |
| Hibernate Ehcache | org.hibernate:hibernate-ehcache | 4.3.9.Final | 6.x | EOL |
| Log4j | log4j:log4j | 1.2.15 | 2.x+ (Log4j2) | **EOL** (Aug 2015) |
| Liquibase | org.liquibase:liquibase-core | 2.0.5 | 4.x | Very outdated |
| Servlet API | javax.servlet:javax.servlet-api | 3.0.1 | Jakarta Servlet 6.x | Outdated |

### Runtime Libraries (MEDIUM severity — outdated)

| Dependency | Group ID | Version | Current | Status |
|-----------|----------|---------|---------|--------|
| Jackson Core | org.codehaus.jackson:jackson-core-asl | 1.9.13 | FasterXML 2.x | **EOL** (Codehaus) |
| Jackson Mapper | org.codehaus.jackson:jackson-mapper-asl | 1.9.13 | FasterXML 2.x | EOL |
| SLF4J API | org.slf4j:slf4j-api | 1.6.0 | 2.x | Outdated |
| SLF4J Log4j12 | org.slf4j:slf4j-log4j12 | 1.6.0 | 2.x | Outdated |
| JCL-over-SLF4J | org.slf4j:jcl-over-slf4j | 1.6.0 | 2.x | Outdated |
| XStream | com.thoughtworks.xstream:xstream | 1.4.3 | 1.4.20+ | Security vulns |
| DOM4J | dom4j:dom4j | 1.6.1 | org.dom4j 2.x | Outdated |
| Xerces | xerces:xercesImpl | 2.8.0 | 2.12+ | Very outdated |
| Commons-IO | commons-io:commons-io | 1.4 | 2.x | Very outdated |
| Commons-Lang3 | org.apache.commons:commons-lang3 | 3.1 | 3.14+ | Outdated |
| Commons-BeanUtils | commons-beanutils:commons-beanutils | 1.7.0 | 1.9.4 | Outdated |
| Commons-Collections | commons-collections:commons-collections | 3.2.2 | 4.x | Outdated |
| Commons-FileUpload | commons-fileupload:commons-fileupload | 1.2.1 | 1.5+ | Security issues |
| ASM | asm:asm | 2.2.3 | 9.x | Extremely outdated |
| ASM Commons | asm:asm-commons | 2.2.3 | 9.x | Extremely outdated |
| CGLIB | cglib:cglib-nodep | 2.2 | 3.x | Outdated |
| Javassist | org.javassist:javassist | 3.19.0-GA | 3.30+ | Outdated |
| HAPI Base | ca.uhn.hapi:hapi-base | 2.0 | 2.5+ | Outdated |
| HAPI v25 | ca.uhn.hapi:hapi-structures-v25 | 2.0 | 2.5+ | Outdated |
| HAPI v26 | ca.uhn.hapi:hapi-structures-v26 | 2.0 | 2.5+ | Outdated |
| Velocity | org.apache.velocity:velocity | 1.6.2 | 2.x | Outdated |
| Velocity Tools | org.apache.velocity:velocity-tools | 2.0 | 3.x | Outdated |
| Ehcache | net.sf.ehcache:ehcache | 2.10.0 | 3.x | Outdated |
| Lucene QueryParser | org.apache.lucene:lucene-queryparser | 4.10.4 | 9.x | Outdated |
| Lucene Queries | org.apache.lucene:lucene-queries | 4.10.4 | 9.x | Outdated |
| Hibernate Search | org.hibernate:hibernate-search-orm | 5.1.2.Final | 7.x | Outdated |
| Hibernate Validator | org.hibernate:hibernate-validator | 4.2.0.Final | 8.x | Outdated |
| javax.validation | javax.validation:validation-api | 1.0.0.GA | Jakarta Validation 3.x | Outdated |
| JavaMail | javax.mail:mail | 1.4.1 | Jakarta Mail 2.x | Outdated |
| MySQL Connector | mysql:mysql-connector-java | 5.1.28 | com.mysql 8.x/9.x | Outdated |
| PostgreSQL | postgresql:postgresql | 9.0-801.jdbc4 | org.postgresql 42.x | Very outdated |
| Groovy | org.codehaus.groovy:groovy-all | 2.4.6 | 4.x | Outdated |
| OWASP Encoder | org.owasp.encoder:encoder | 1.2 | 1.2.3 | Slightly outdated |
| ReflectUtils | org.azeckoski:reflectutils | 0.9.14 | — | Unmaintained |

### Database Drivers

| Driver | Version | Scope | Notes |
|--------|---------|-------|-------|
| MySQL Connector/J | 5.1.28 | runtime | Outdated (current: 8.x) |
| PostgreSQL | 9.0-801.jdbc4 | runtime | Very outdated (current: 42.x) |
| H2 | 1.4.187 | test/dev | Outdated (current: 2.x) |
| MySQL Connector MXJ | 5.0.11 | integration-test | Embedded MySQL for tests |

## Build/Dev Dependencies (LOW severity)

### Maven Plugins

| Plugin | Version | Current | Notes |
|--------|---------|---------|-------|
| maven-compiler-plugin | 2.1 | 3.13+ | Very outdated |
| maven-surefire-plugin | 2.18.1 | 3.2+ | Outdated |
| maven-war-plugin | 2.4 | 3.4+ | Outdated |
| maven-jar-plugin | 2.3.2 | 3.4+ | Outdated |
| maven-resources-plugin | 2.4 | 3.3+ | Outdated |
| maven-assembly-plugin | 2.2-beta-5 | 3.7+ | Very outdated (beta!) |
| maven-source-plugin | 2.1.2 | 3.3+ | Outdated |
| maven-javadoc-plugin | 2.10.3 | 3.6+ | Outdated |
| maven-release-plugin | 2.5 | 3.0+ | Outdated |
| maven-eclipse-plugin | 2.8 | — | Deprecated |
| maven-checkstyle-plugin | 2.9.1 | 3.3+ | Outdated |
| maven-antrun-plugin | 1.4 | 3.1+ | Outdated |
| license-maven-plugin | 2.6 | 4.x+ | Outdated |
| buildnumber-maven-plugin | 1.1 | 3.2+ | Outdated |
| build-helper-maven-plugin | 1.5 | 3.5+ | Outdated |
| maven-java-formatter-plugin | 0.3 | — | Very old |
| jacoco-maven-plugin | 0.7.4 | 0.8.12 | Outdated |
| sonar-maven-plugin | 4.5.4 | — | Codehaus, outdated |
| antlr-maven-plugin | 2.1 | — | Codehaus, outdated |
| jetty-maven-plugin | 9.3.3 | 12.x | Outdated |
| tomcat7-maven-plugin | 2.2 | — | Outdated |

### Test Dependencies

| Dependency | Version | Current | Notes |
|-----------|---------|---------|-------|
| JUnit | 4.11 | JUnit 5 (5.10+) | Outdated |
| Mockito | 1.9.5 | 5.x | Very outdated |
| PowerMock | 1.5 | — | Incompatible with modern JDK |
| Hamcrest Core | 1.3 | 2.2 | Outdated |
| Hamcrest Library | 1.3 | 2.2 | Outdated |
| Spring Test | 4.1.4.RELEASE | 6.x | EOL |
| H2 | 1.4.187 | 2.x | Outdated |
| DBUnit | 2.4.7 | 2.7+ | Outdated |
| XMLUnit | 1.3 | 2.x | Outdated |
| Databene Benerator | 0.5.9 | — | Unmaintained |
| LambdaJ | 2.2 | — | Unmaintained (Java 8 streams replaced) |
| JUnit Benchmarks | 0.7.0 | JMH | Replaced by JMH |

## Liquibase Extensions

| Extension | Version | Purpose |
|-----------|---------|---------|
| modify-column | 2.0.2 | Custom column modification |
| identity-insert | 1.2.1 | Identity insert support |
| type-converter | 1.0.1 | Custom type conversion |

## Related Documentation

- [System Overview](system-overview.md)
- [Components](components.md)
- [Dependency Analysis](../analysis/dependency-analysis.md)
- [Technical Debt Report](../technical-debt-report.md)
- [Outdated Components](../technical-debt/outdated-components.md)
