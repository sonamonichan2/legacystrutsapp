# Maven Modules Reference — OpenMRS Core 2.0.3

## Module Hierarchy

```
openmrs (pom) — v2.0.3
├── tools       (org.openmrs.tools:openmrs-tools)
├── test        (org.openmrs.test:openmrs-test)
├── api         (org.openmrs.api:openmrs-api)
├── web         (org.openmrs.web:openmrs-web)
└── webapp      (org.openmrs.web:openmrs-webapp)
```

## Module Details

### tools
| Property | Value |
|----------|-------|
| Group ID | org.openmrs.tools |
| Artifact ID | openmrs-tools |
| Packaging | jar |
| Purpose | Build tools: checkstyle configs, formatter configs, custom Javadoc taglets |
| Dependencies | JDK tools.jar (profile-activated) |
| Source Files | Minimal (ShouldTaglet, checkstyle configs) |

### test
| Property | Value |
|----------|-------|
| Group ID | org.openmrs.test |
| Artifact ID | openmrs-test |
| Packaging | pom |
| Purpose | Aggregates all test dependencies for reuse |
| Key Dependencies | JUnit 4.11, Mockito 1.9.5, PowerMock 1.5, Spring Test 4.1.4, H2 1.4.187, DBUnit 2.4.7, XMLUnit 1.3, Hamcrest 1.3, Databene Benerator 0.5.9 |

### api
| Property | Value |
|----------|-------|
| Group ID | org.openmrs.api |
| Artifact ID | openmrs-api |
| Packaging | jar |
| Purpose | Core domain model, services, DAOs, validators, HL7, scheduler, modules |
| Source Files | ~649 main, ~288 test |
| Key Dependencies | Spring (core, beans, context, aop, orm, tx, jdbc), Hibernate (core, c3p0, ehcache, search, validator), Liquibase, HAPI, Jackson (Codehaus), Log4j, SLF4J, XStream, DOM4J, Velocity, Groovy, Ehcache, Lucene, ASM, CGLIB, Javassist, JavaMail, javax.validation, commons-*, OWASP Encoder |
| Plugins | maven-jar-plugin (test-jar), maven-surefire-plugin, antlr-maven-plugin, maven-java-formatter-plugin |

### web
| Property | Value |
|----------|-------|
| Group ID | org.openmrs.web |
| Artifact ID | openmrs-web |
| Packaging | jar |
| Purpose | Web layer: filters, controllers, servlets, initialization wizard |
| Source Files | 39 main, 11 test |
| Dependencies | openmrs-api (compile), Servlet API 3.0.1, JSP API 2.0, JSTL 1.1.2, Spring WebMVC, Spring OXM, commons-fileupload, MySQL/PostgreSQL drivers, Liquibase, Velocity Tools, OWASP Encoder, taglibs |

### webapp
| Property | Value |
|----------|-------|
| Group ID | org.openmrs.web |
| Artifact ID | openmrs-webapp |
| Packaging | war |
| Purpose | Final deployable WAR — web.xml, JSP resources, Liquibase demo data |
| Dependencies | openmrs-api (compile), openmrs-web (compile) |
| Plugins | maven-war-plugin 2.4, jetty-maven-plugin 9.3.3, tomcat7-maven-plugin 2.2, maven-antrun-plugin, maven-checkstyle-plugin |
| Profiles | jrebel (hot-reload), integration-test (MySQL), h2 (H2 database), install-h2 |

## Build Profiles

| Profile | Module | Purpose |
|---------|--------|---------|
| skip-default-test | root | Skip default test execution |
| integration-test | root | Run integration tests (*IT.java) |
| performance-test | root/api | Run performance tests (*PT.java) |
| release-test | root | Include release-test module |
| sonar | root | SonarQube analysis with JaCoCo |
| release | api/web/webapp | Generate source + javadoc JARs |
| jrebel | webapp | JRebel hot-reload support |
| h2 | webapp | H2 database dependency |
| install-h2 | webapp | H2 with installation script |

## Related Documentation

- [Project Overview](../project-overview.md)
- [Dependencies](../architecture/dependencies.md)
- [Program Structure](program-structure.md)
