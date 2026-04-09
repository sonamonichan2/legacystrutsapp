# System Architecture Overview — OpenMRS Core 2.0.3

## High-Level Architecture

OpenMRS Core is a **layered monolithic** Java web application packaged as a WAR file. It follows a traditional three-tier architecture with a plugin/module extension system.

```
┌─────────────────────────────────────────────────────────────┐
│                    Servlet Container (Tomcat/Jetty)          │
│  ┌───────────────────────────────────────────────────────┐  │
│  │                  webapp (WAR)                          │  │
│  │  ┌─────────────────────────────────────────────────┐  │  │
│  │  │           Web Layer (web module)                 │  │  │
│  │  │  Filters │ Controllers │ Servlets │ Listeners   │  │  │
│  │  ├─────────────────────────────────────────────────┤  │  │
│  │  │         Service Layer (api module)               │  │  │
│  │  │  Service Interfaces │ Implementations │ AOP     │  │  │
│  │  ├─────────────────────────────────────────────────┤  │  │
│  │  │          DAO Layer (api module)                  │  │  │
│  │  │  DAO Interfaces │ Hibernate DAOs │ HQL/Criteria │  │  │
│  │  ├─────────────────────────────────────────────────┤  │  │
│  │  │       Domain Model (api module)                  │  │  │
│  │  │  Entities │ Value Objects │ Enums               │  │  │
│  │  └─────────────────────────────────────────────────┘  │  │
│  │  ┌─────────────────────────────────────────────────┐  │  │
│  │  │        Module/Plugin System                      │  │  │
│  │  │  ModuleFactory │ ModuleClassLoader │ Extensions │  │  │
│  │  └─────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────┘  │
│  ┌──────────────┐  ┌──────────────┐  ┌────────────────┐    │
│  │ MySQL/PgSQL  │  │  HL7 Source  │  │ OpenMRS Modules│    │
│  └──────────────┘  └──────────────┘  └────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

## Architectural Style

The application is a **classic layered monolith** with the following characteristics:

1. **Service-DAO Pattern**: Every domain aggregate has a Service interface, an implementation, a DAO interface, and a Hibernate DAO.
2. **AOP-Based Cross-Cutting Concerns**: Authorization, logging, and audit-field population are handled via Spring AOP advice classes.
3. **Service Locator Pattern**: The `Context` class acts as a centralized service locator — `Context.getPatientService()`, `Context.getConceptService()`, etc. This introduces tight coupling throughout the codebase.
4. **XML-Driven Configuration**: Both Spring bean wiring (`applicationContext-service.xml` — 689 lines) and Hibernate entity mappings (`hibernate.cfg.xml` — 84 mappings) are in XML.
5. **Module/Plugin Architecture**: A custom classloader-based module system (`ModuleFactory`, `ModuleClassLoader`) allows dynamic extension loading at runtime.
6. **WAR-Based Deployment**: The final artifact is a traditional WAR deployed into a Servlet container.

## Technology Stack Detail

### Core Framework

| Component | Technology | Version | Status |
|-----------|-----------|---------|--------|
| Java | Oracle JDK / OpenJDK | 1.8 | EOL for free Oracle JDK |
| Spring Core | spring-core, spring-beans, spring-context | 4.1.4.RELEASE | EOL (Dec 2020) |
| Spring AOP | spring-aop | 4.1.4.RELEASE | EOL |
| Spring ORM | spring-orm, spring-tx, spring-jdbc | 4.1.4.RELEASE | EOL |
| Spring Web | spring-web, spring-webmvc, spring-oxm | 4.1.4.RELEASE | EOL |
| Hibernate Core | hibernate-core | 4.3.9.Final | EOL |
| Hibernate C3P0 | hibernate-c3p0 | 4.3.9.Final | EOL |
| Hibernate Ehcache | hibernate-ehcache | 4.3.9.Final | EOL |
| Hibernate Search | hibernate-search-orm | 5.1.2.Final | Outdated |
| Hibernate Validator | hibernate-validator | 4.2.0.Final | Outdated |
| Lucene | lucene-queryparser, lucene-queries | 4.10.4 | Outdated |
| Liquibase | liquibase-core | 2.0.5 | Very outdated |
| Ehcache | ehcache | 2.10.0 | Outdated |

### Serialization & Data Handling

| Component | Version | Notes |
|-----------|---------|-------|
| Jackson (Codehaus) | 1.9.13 | EOL — replaced by com.fasterxml.jackson |
| XStream | 1.4.3 | Known security vulnerabilities |
| DOM4J | 1.6.1 | Outdated |
| Xerces | 2.8.0 | Very outdated |

### Integration & Communication

| Component | Version | Notes |
|-----------|---------|-------|
| HAPI HL7 | 2.0 | HL7v2.5/v2.6 message processing |
| JavaMail | 1.4.1 | Outdated, pre-Jakarta |
| Velocity | 1.6.2 | Template engine for notifications |

### Bytecode & Proxying

| Component | Version | Notes |
|-----------|---------|-------|
| ASM | 2.2.3 | Extremely outdated (current: 9.x) |
| CGLIB | 2.2 | Outdated (current: 3.x) |
| Javassist | 3.19.0-GA | Outdated |

### Database Drivers

| Driver | Version | Notes |
|--------|---------|-------|
| MySQL Connector/J | 5.1.28 | Outdated (current: 8.x/9.x) |
| PostgreSQL | 9.0-801.jdbc4 | Very outdated |
| H2 | 1.4.187 | Test/dev only |

### Logging

| Component | Version | Notes |
|-----------|---------|-------|
| Log4j | 1.2.15 | EOL (Aug 2015), security concerns |
| SLF4J API | 1.6.0 | Outdated |
| SLF4J-Log4j12 | 1.6.0 | Bridge to Log4j 1 |
| JCL-over-SLF4J | 1.6.0 | Commons-logging bridge |

## Deployment Model

### Production Deployment
- **Artifact**: Single WAR file (`openmrs.war`)
- **Container**: Tomcat 7+ / any Servlet 3.0+ container
- **Database**: MySQL 5.x (primary), PostgreSQL 9.x (alternate)
- **OS Target**: Linux (AWS EC2 compatible)
- **JVM**: Java 8 runtime

### Development Setup
- **Jetty**: Embedded via `jetty-maven-plugin` 9.3.3.v20150827
- **Tomcat**: Via `tomcat7-maven-plugin` 2.2
- **Database**: H2 (in-memory) for testing; MySQL for development
- **JRebel**: Optional profile for hot-reloading

### Schema Management
- `liquibase-schema-only.xml` — Schema creation
- `liquibase-update-to-latest.xml` — Incremental updates
- `liquibase-core-data.xml` — Core reference data
- Custom Liquibase extensions: `modify-column`, `identity-insert`, `type-converter`

## Spring Configuration Architecture

The application uses **XML-based Spring configuration** (`applicationContext-service.xml`):

1. **Auto-proxy creation** via `DefaultAdvisorAutoProxyCreator`
2. **Transaction management** via `TransactionAttributeSourceAdvisor` + `TransactionInterceptor`
3. **Hibernate SessionFactory** configured with XML mappings from `hibernate.cfg.xml`
4. **Service bean definitions** — each service is explicitly wired with DAO and AOP advice
5. **AOP Advice chain**: AuthorizationAdvice → LoggingAdvice → RequiredDataAdvice → Service method

## Key Architectural Decisions

1. **Service Locator (Context)**: All service access goes through `Context.getXxxService()`. This creates a hidden dependency graph and makes dependency injection difficult to trace.
2. **XML Hibernate Mappings**: All 84 entity mappings use `.hbm.xml` files rather than JPA annotations, coupling the persistence layer to Hibernate-specific APIs.
3. **Flat Package Structure**: Domain entities, service interfaces, DAOs, and cross-cutting concerns all live under `org.openmrs.*` without clear module boundaries.
4. **Module System**: Custom classloading infrastructure for third-party plugins adds complexity but provides extensibility.
5. **No REST Controller Layer**: The web module contains primarily filters and initialization logic — REST endpoints are typically provided by the `webservices.rest` module (external).

## Related Documentation

- [Project Overview](../project-overview.md)
- [Components](components.md)
- [Dependencies](dependencies.md)
- [Architectural Patterns](patterns.md)
- [Technical Debt Report](../technical-debt-report.md)
