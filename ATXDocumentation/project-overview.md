# OpenMRS Core 2.0.3 — Project Overview

## Project Identity

| Attribute | Value |
|-----------|-------|
| **Project Name** | OpenMRS Core |
| **Version** | 2.0.3 |
| **Group ID** | org.openmrs |
| **Artifact ID** | openmrs |
| **Packaging** | pom (multi-module), final artifact: WAR |
| **License** | Mozilla Public License 2.0 with Healthcare Disclaimer |
| **Repository** | https://github.com/openmrs/openmrs-core |
| **CI** | Bamboo — https://ci.openmrs.org/browse/TRUNK-MASTER |
| **Issue Tracker** | JIRA — http://tickets.openmrs.org/ |

## Purpose

OpenMRS Core is an open-source electronic medical records (EMR) platform designed for healthcare environments, particularly in low-resource settings. It provides a patient-centric data model for recording encounters, observations, orders, programs, and clinical workflows. The platform supports extensibility through a module system that allows third-party plugins to add functionality.

## Technology Stack Summary

| Layer | Technology | Version |
|-------|-----------|---------|
| **Language** | Java | 1.8 (source/target) |
| **Build Tool** | Apache Maven | 3.x (multi-module) |
| **Core Framework** | Spring Framework | 4.1.4.RELEASE |
| **ORM** | Hibernate | 4.3.9.Final |
| **Search** | Hibernate Search + Lucene | 5.1.2.Final / 4.10.4 |
| **Database Migration** | Liquibase | 2.0.5 |
| **Caching** | Ehcache | 2.10.0 |
| **Logging** | Log4j 1.x + SLF4J | 1.2.15 / 1.6.0 |
| **Serialization** | Jackson (Codehaus) + XStream | 1.9.13 / 1.4.3 |
| **HL7 Integration** | HAPI | 2.0 |
| **Template Engine** | Velocity | 1.6.2 |
| **Servlet Container** | Servlet 3.0.1 / JSP 2.0 / JSTL 1.1.2 | — |
| **Dev Containers** | Jetty / Tomcat7 Maven Plugin | 9.3.3 / 2.2 |
| **Database Drivers** | MySQL / PostgreSQL / H2 | 5.1.28 / 9.0-801 / 1.4.187 |
| **Scripting** | Groovy | 2.4.6 |
| **Security** | OWASP Encoder | 1.2 |
| **Validation** | Hibernate Validator / javax.validation | 4.2.0.Final / 1.0.0.GA |

## Maven Module Structure

```
openmrs (pom) — Root aggregator
├── tools       (jar)  — Build tools, code formatters, doclets
├── test        (pom)  — Shared test dependency aggregation
├── api         (jar)  — Core domain model, services, DAOs, business logic
├── web         (jar)  — Web filters, controllers, servlet integration
└── webapp      (war)  — Final WAR packaging, web resources, deployment descriptors
```

### Module Dependency Chain
```
tools (standalone)
test (aggregates test libs: JUnit, Mockito, PowerMock, H2, DBUnit)
api  → test (test scope)
web  → api, test (test scope)
webapp → api, web, test (test scope)  [WAR packaging]
```

## Codebase Metrics

| Metric | Value |
|--------|-------|
| Total Source Lines of Code | ~210,000 |
| Domain Entity Classes | ~105 |
| Service Interfaces | ~20 |
| Service Implementations | ~20 |
| DAO Interfaces | ~20 |
| Hibernate DAOs | ~20 |
| Validators | ~50+ |
| Hibernate XML Mappings | 84 |
| Spring XML Config Lines | ~689 |
| Web Layer Files | ~39 |

## Deployment Model

- **Packaging**: WAR file deployed on Servlet 3.0+ containers
- **Primary Target**: Apache Tomcat 7+
- **Development Servers**: Jetty 9.3.3 (Maven plugin), Tomcat 7 (Maven plugin)
- **Database**: MySQL 5.x (primary), PostgreSQL 9.x (supported), H2 (testing/dev)
- **Schema Management**: Liquibase-based migrations (liquibase-schema-only.xml, liquibase-update-to-latest.xml, liquibase-core-data.xml)

## Related Documentation

- [System Architecture Overview](architecture/system-overview.md)
- [Component Documentation](architecture/components.md)
- [Dependencies](architecture/dependencies.md)
- [Technical Debt Report](technical-debt-report.md)
- [Migration Plan](migration/component-order.md)
