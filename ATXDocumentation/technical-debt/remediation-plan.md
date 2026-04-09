# Remediation Plan — OpenMRS Core 2.0.3

## Priority 1 — HIGH: EOL Runtimes and Frameworks

### 1.1 Java 8 → Java 21

**Severity**: High | **Effort**: Significant | **Risk**: Medium

- Upgrade `javaCompilerVersion` from `1.8` to `21`
- Update `maven-compiler-plugin` from 2.1 to 3.13+ with `<release>21</release>`
- Remove PowerMock dependency (incompatible with Java 9+ module system)
- Replace `sun.misc.*` and internal API usages if any
- Address reflection access warnings
- Update all Maven plugins to Java 21-compatible versions
- **Recommended**: Use `AWS/java-version-upgrade` transformation

### 1.2 Log4j 1.2.15 → SLF4J 2.x + Logback

**Severity**: High | **Effort**: Moderate | **Risk**: Low

- Replace `log4j:log4j:1.2.15` with `ch.qos.logback:logback-classic`
- Update SLF4J from 1.6.0 to 2.x
- Remove `slf4j-log4j12` bridge, add `logback-classic`
- Convert `log4j.xml` configuration to `logback.xml`
- Update all `org.apache.log4j.*` imports to `org.slf4j.*`
- **Recommended**: Use `AWS/early-access-log4j-to-slf4j-migration` transformation

### 1.3 Spring Framework 4.1.4 → Spring 6.x / Spring Boot 3.x

**Severity**: High | **Effort**: Very significant | **Risk**: High

- Phase approach recommended:
  1. First migrate to Spring 5.3.x (last javax version)
  2. Then migrate to Spring 6.x with Jakarta namespace
- Convert XML configuration to Java `@Configuration` classes
- Replace `DefaultAdvisorAutoProxyCreator` with Spring Boot auto-configuration
- Replace explicit bean definitions with component scanning + `@Service`/`@Repository`
- Update Spring schema references in remaining XML

### 1.4 Hibernate 4.3.9 → Hibernate 6.x

**Severity**: High | **Effort**: Very significant | **Risk**: High

- Phase approach:
  1. Convert 84 `.hbm.xml` files to JPA annotations on entities
  2. Migrate from `Session` to `EntityManager` where possible
  3. Update HQL queries for Hibernate 6 changes
  4. Replace `Criteria` API (deprecated) with JPA Criteria or HQL
- Replace `DbSessionFactory` wrapper with standard JPA infrastructure
- Update Hibernate interceptors to new API

### 1.5 Servlet API 3.0.1 → Jakarta Servlet 6.x

**Severity**: High | **Effort**: Moderate | **Risk**: Medium

- Replace `javax.servlet.*` imports with `jakarta.servlet.*`
- Update `web.xml` namespace
- Update JSP/JSTL dependencies to Jakarta equivalents

### 1.6 Liquibase 2.0.5 → Liquibase 4.x

**Severity**: High | **Effort**: Moderate | **Risk**: Medium

- Update Liquibase dependency to 4.x
- Review changelog XML for deprecated syntax
- Test all migration scripts against target databases
- Update custom Liquibase extensions (modify-column, identity-insert, type-converter) or find alternatives

## Priority 2 — MEDIUM: Outdated Runtime Dependencies

### 2.1 Jackson Replacement

- Replace `org.codehaus.jackson:jackson-*-asl:1.9.13` with `com.fasterxml.jackson:jackson-*:2.17.x`
- Update all `@org.codehaus.jackson` annotations to `@com.fasterxml.jackson`
- Update custom serializers/deserializers

### 2.2 Security-Critical Updates

- **XStream** 1.4.3 → 1.4.20+: Address known CVEs, enable security framework
- **Commons-FileUpload** 1.2.1 → 2.0 (Jakarta): Address security vulnerabilities
- **Commons-BeanUtils** 1.7.0 → 1.9.4: Security patches

### 2.3 Database Drivers

- **MySQL** 5.1.28 → com.mysql:mysql-connector-j:8.x: New group ID and artifact
- **PostgreSQL** 9.0-801.jdbc4 → org.postgresql:postgresql:42.x: New group ID

### 2.4 Bytecode Libraries

- **ASM** 2.2.3 → org.ow2.asm:asm:9.x: New group ID, major version jump
- **CGLIB** 2.2 → cglib:cglib:3.x
- **Javassist** 3.19.0 → 3.30.x

### 2.5 Other Runtime Libraries

- Commons-IO 1.4 → 2.x
- Commons-Lang3 3.1 → 3.14+
- Commons-Collections 3.2.2 → 4.x (org.apache.commons:commons-collections4)
- Ehcache 2.10.0 → 3.x (org.ehcache)
- Velocity 1.6.2 → 2.x (org.apache.velocity.engine)
- JavaMail 1.4.1 → Jakarta Mail 2.x
- javax.validation 1.0.0 → Jakarta Validation 3.x
- Hibernate Validator 4.2.0 → 8.x
- Hibernate Search 5.1.2 → 7.x (complete rewrite)
- Lucene 4.10.4 → 9.x
- HAPI 2.0 → 2.5+
- Groovy 2.4.6 → 4.x (org.apache.groovy)

## Priority 3 — LOW: Build/Dev Dependencies

### 3.1 Test Framework Migration

- JUnit 4.11 → JUnit 5 (Jupiter)
- Mockito 1.9.5 → Mockito 5.x
- Remove PowerMock entirely (use Mockito's inline mock maker)
- Hamcrest 1.3 → 2.x or AssertJ
- DBUnit 2.4.7 → 2.7+

### 3.2 Maven Plugin Updates

- maven-compiler-plugin → 3.13+
- maven-surefire-plugin → 3.2+ (JUnit 5 support)
- maven-war-plugin → 3.4+
- maven-jar-plugin → 3.4+
- All other plugins to latest stable versions
- Remove deprecated maven-eclipse-plugin

### 3.3 Build Infrastructure

- Remove Ant build files (build.xml, liquibase.build.xml) — Maven-only build
- Update CI configuration for Java 21
- Consider Maven Wrapper for reproducible builds

## Architectural Remediation

### A1. Replace Service Locator with Dependency Injection

- Replace `Context.getService()` calls with constructor injection
- Use `@Autowired` / `@Inject` on service implementations
- Remove static service accessor methods from `Context.java`
- Introduce module-level interfaces for cross-module communication

### A2. Package Restructuring

- Move from flat `org.openmrs.*` to domain-based packages
- Example: `org.openmrs.patient.domain`, `org.openmrs.patient.application`, `org.openmrs.patient.infrastructure`
- Enforce module boundaries with Java module system or ArchUnit tests

### A3. XML to Annotation Migration

- Convert `applicationContext-service.xml` to `@Configuration` classes
- Convert `.hbm.xml` to JPA annotations
- Use Spring Boot auto-configuration where possible

### A4. Break Cyclic Dependencies

- Introduce domain events for cross-module notifications
- Extract shared interfaces into shared-kernel module
- Use event-driven communication for service-to-service calls

## Related Documentation

- [Summary](summary.md)
- [Outdated Components](outdated-components.md)
- [Maintenance Burden](maintenance-burden.md)
- [Migration Plan](../migration/component-order.md)
