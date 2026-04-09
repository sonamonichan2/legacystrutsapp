# Outdated Components — OpenMRS Core 2.0.3

## HIGH Severity — EOL/Deprecated Runtimes and Frameworks

| Component | Current Version | Latest Stable | EOL Date | Risk |
|-----------|----------------|--------------|----------|------|
| Java (JDK) | 1.8 | 21 (LTS) | Jan 2019 (free Oracle) | No modern features, declining patch support |
| Spring Framework | 4.1.4.RELEASE | 6.1.x | Dec 2020 | No security patches, blocks Boot 3.x |
| Hibernate Core | 4.3.9.Final | 6.4.x | EOL | No patches, Hibernate-specific API lock-in |
| Hibernate C3P0 | 4.3.9.Final | 6.4.x | EOL | Connection pooling issues |
| Hibernate Ehcache | 4.3.9.Final | N/A (replaced) | EOL | Ehcache integration deprecated |
| Servlet API | 3.0.1 (javax) | 6.0 (Jakarta) | — | javax→Jakarta namespace migration needed |
| JSP API | 2.0 (javax) | 3.1 (Jakarta) | — | javax→Jakarta namespace migration needed |
| Log4j | 1.2.15 | 2.23.x (Log4j2) | Aug 2015 | **Security risk**, no patches |
| Liquibase | 2.0.5 | 4.27.x | — | Very outdated, missing modern features |

## MEDIUM Severity — Outdated Runtime/Production Dependencies

### Serialization & Data

| Component | Current | Latest | Gap | Notes |
|-----------|---------|--------|-----|-------|
| Jackson Core ASL | 1.9.13 (Codehaus) | 2.17.x (FasterXML) | Complete replacement | Codehaus is **EOL** |
| Jackson Mapper ASL | 1.9.13 (Codehaus) | 2.17.x (FasterXML) | Complete replacement | Codehaus is **EOL** |
| XStream | 1.4.3 | 1.4.20+ | 17 patch versions | Known CVEs (XXE, RCE) |
| DOM4J | 1.6.1 | 2.1.4 (org.dom4j) | Major version + group change | org.dom4j replaces dom4j |
| Xerces | 2.8.0 | 2.12.2 | 4 minor versions | Very outdated |

### Logging

| Component | Current | Latest | Gap | Notes |
|-----------|---------|--------|-----|-------|
| SLF4J API | 1.6.0 | 2.0.x | Major version | API changes in 2.x |
| SLF4J-Log4j12 | 1.6.0 | 2.0.x | Major version | Should migrate to Logback |
| JCL-over-SLF4J | 1.6.0 | 2.0.x | Major version | — |

### Commons Libraries

| Component | Current | Latest | Gap | Notes |
|-----------|---------|--------|-----|-------|
| Commons-IO | 1.4 | 2.16.x | Major version | Very outdated |
| Commons-Lang3 | 3.1 | 3.14.x | 13 minor versions | Significant gap |
| Commons-BeanUtils | 1.7.0 | 1.9.4 | 2 minor versions | Outdated |
| Commons-Collections | 3.2.2 | 4.4 (commons-collections4) | Major version + artifact change | Should migrate to 4.x |
| Commons-FileUpload | 1.2.1 | 1.5 / 2.0 (Jakarta) | Known CVEs | **Security concern** |

### Bytecode & Proxying

| Component | Current | Latest | Gap | Notes |
|-----------|---------|--------|-----|-------|
| ASM | 2.2.3 | 9.7 | **7 major versions** | Extremely outdated |
| ASM Commons | 2.2.3 | 9.7 | 7 major versions | Extremely outdated |
| CGLIB | 2.2 | 3.3.0 | 1 major version | Outdated |
| Javassist | 3.19.0-GA | 3.30.x | 11 minor versions | Outdated |

### Database Drivers

| Component | Current | Latest | Gap | Notes |
|-----------|---------|--------|-----|-------|
| MySQL Connector/J | 5.1.28 | 9.0.x / 8.4.x | Major version + group change | com.mysql replaces mysql |
| PostgreSQL | 9.0-801.jdbc4 | 42.7.x | Complete replacement | org.postgresql replaces postgresql |
| H2 | 1.4.187 | 2.2.x | Major version | Test scope only |

### HL7 Integration

| Component | Current | Latest | Gap | Notes |
|-----------|---------|--------|-----|-------|
| HAPI Base | 2.0 | 2.5.x | 5 minor versions | Outdated |
| HAPI v25 Structures | 2.0 | 2.5.x | 5 minor versions | — |
| HAPI v26 Structures | 2.0 | 2.5.x | 5 minor versions | — |

### Template & Scripting

| Component | Current | Latest | Gap | Notes |
|-----------|---------|--------|-----|-------|
| Velocity | 1.6.2 | 2.3 | Major version | org.apache.velocity.engine replaces velocity |
| Velocity Tools | 2.0 | 3.1 | Major version | — |
| Groovy | 2.4.6 | 4.0.x | 2 major versions | org.apache.groovy replaces org.codehaus.groovy |

### Caching & Search

| Component | Current | Latest | Gap | Notes |
|-----------|---------|--------|-----|-------|
| Ehcache | 2.10.0 | 3.10.x | Major version | org.ehcache replaces net.sf.ehcache |
| Hibernate Search | 5.1.2.Final | 7.1.x | 2 major versions | Complete rewrite in 6.x |
| Lucene QueryParser | 4.10.4 | 9.10.x | 5 major versions | Outdated |
| Lucene Queries | 4.10.4 | 9.10.x | 5 major versions | — |

### Validation

| Component | Current | Latest | Gap | Notes |
|-----------|---------|--------|-----|-------|
| javax.validation | 1.0.0.GA | 3.0.x (Jakarta) | 2 major + namespace | Jakarta Validation |
| Hibernate Validator | 4.2.0.Final | 8.0.x | 4 major versions | — |

### Other Runtime

| Component | Current | Latest | Gap | Notes |
|-----------|---------|--------|-----|-------|
| JavaMail | 1.4.1 (javax.mail) | 2.1.x (Jakarta) | Major + namespace | Jakarta Mail |
| OWASP Encoder | 1.2 | 1.2.3 | 1 patch | Slightly outdated |
| ReflectUtils | 0.9.14 | — | — | Appears unmaintained |
| LambdaJ | 2.2 | — | — | Unmaintained, replaced by Java 8 streams |

## LOW Severity — Outdated Dev/Build Dependencies

### Test Libraries

| Component | Current | Latest | Gap | Notes |
|-----------|---------|--------|-----|-------|
| JUnit | 4.11 | 5.10.x (JUnit 5) | Major version | JUnit 5 with Jupiter |
| Mockito | 1.9.5 | 5.12.x | 4 major versions | Very outdated |
| PowerMock | 1.5 | 2.0.9 | 1 major version | Incompatible with modern JDK |
| Hamcrest Core | 1.3 | 2.2 | 1 major version | — |
| Hamcrest Library | 1.3 | 2.2 | 1 major version | — |
| Spring Test | 4.1.4.RELEASE | 6.1.x | EOL | Follows Spring version |
| DBUnit | 2.4.7 | 2.7.x | 3 minor versions | — |
| XMLUnit | 1.3 | 2.9.x | Major version | — |
| Databene Benerator | 0.5.9 | — | — | Unmaintained |
| JUnit Benchmarks | 0.7.0 | — | — | Replaced by JMH |

### Maven Plugins

| Plugin | Current | Latest | Gap |
|--------|---------|--------|-----|
| maven-compiler-plugin | 2.1 | 3.13.x | Major + many minors |
| maven-surefire-plugin | 2.18.1 | 3.2.x | Major version |
| maven-war-plugin | 2.4 | 3.4.x | Major version |
| maven-jar-plugin | 2.3.2 | 3.4.x | Major version |
| maven-resources-plugin | 2.4 | 3.3.x | Major version |
| maven-assembly-plugin | 2.2-beta-5 | 3.7.x | Major + was beta |
| maven-source-plugin | 2.1.2 | 3.3.x | Major version |
| maven-javadoc-plugin | 2.10.3 | 3.6.x | Major version |
| maven-release-plugin | 2.5 | 3.0.x | Major version |
| maven-checkstyle-plugin | 2.9.1 | 3.3.x | Major version |
| license-maven-plugin | 2.6 | 4.5 | 2 major versions |
| buildnumber-maven-plugin | 1.1 | 3.2.x | 2 major versions |
| build-helper-maven-plugin | 1.5 | 3.5.x | 2 major versions |
| jacoco-maven-plugin | 0.7.4 | 0.8.12 | Many minors |
| maven-eclipse-plugin | 2.8 | — | Deprecated |
| jetty-maven-plugin | 9.3.3 | 12.x | 3 major versions |
| tomcat7-maven-plugin | 2.2 | — | Outdated (Tomcat 7 EOL) |

## Related Documentation

- [Summary](summary.md)
- [Maintenance Burden](maintenance-burden.md)
- [Remediation Plan](remediation-plan.md)
- [Dependencies](../architecture/dependencies.md)
