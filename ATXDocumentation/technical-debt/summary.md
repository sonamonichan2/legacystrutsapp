# Technical Debt Summary — OpenMRS Core 2.0.3

## Overview

OpenMRS Core 2.0.3 carries substantial technical debt accumulated from a codebase that was designed and built in the Java 6/7 era and last significantly updated around 2016-2017. The debt falls into three categories: (1) EOL/deprecated runtimes and frameworks, (2) outdated runtime dependencies, and (3) architectural issues that impede modernization.

## Severity Distribution

| Severity | Count | Description |
|----------|-------|-------------|
| **High** | 6 | EOL/deprecated runtimes and frameworks |
| **Medium** | 25+ | Outdated runtime/production dependencies |
| **Low** | 15+ | Outdated dev/build dependencies |
| **Architectural** | 5 | Structural and design debt |

## High Severity Findings

1. **Java 8 (source/target 1.8)** — Free Oracle JDK EOL since Jan 2019. Missing modern features (records, sealed classes, virtual threads, pattern matching). Target: Java 21.
2. **Spring Framework 4.1.4.RELEASE** — EOL since Dec 2020. No security patches. Blocks Spring Boot 3.x adoption. Target: Spring 6.x via Spring Boot 3.x.
3. **Hibernate 4.3.9.Final** — EOL. No patches. Uses Hibernate-specific APIs instead of JPA. Target: Hibernate 6.x with JPA 3.x.
4. **Servlet API 3.0.1 (javax.servlet)** — javax namespace outdated. Blocks Jakarta EE migration. Target: Jakarta Servlet 6.x.
5. **Log4j 1.2.15** — EOL since Aug 2015. Known security concerns. No patches available. Target: SLF4J 2.x + Logback.
6. **Liquibase 2.0.5** — Very outdated (current: 4.x). Missing modern features (rollback improvements, JSON/YAML support). Target: Liquibase 4.x.

## Medium Severity Findings

See [Outdated Components](outdated-components.md) for the complete list of 25+ outdated runtime dependencies with exact versions and upgrade paths.

Key items:
- Jackson 1.9.13 (Codehaus — completely EOL)
- XStream 1.4.3 (known CVEs)
- Commons-FileUpload 1.2.1 (security issues)
- ASM 2.2.3 (extremely outdated, 7 major versions behind)
- MySQL Connector 5.1.28 (outdated)
- PostgreSQL 9.0-801.jdbc4 (very outdated)

## Architectural Debt

See [Maintenance Burden](maintenance-burden.md) for detailed analysis.

Key items:
- **Service Locator Pattern** — ~308 Context.getService() calls create hidden coupling
- **84 Hibernate XML mapping files** — verbose, duplicates entity information
- **689-line Spring XML config** — hard to navigate and maintain
- **Flat package structure** — no module boundaries
- **5+ cyclic service dependencies** — blocks clean modularization

## Remediation Priority

1. **First**: Java version upgrade (8 → 21) — enables all other upgrades
2. **Second**: Log4j migration (1.x → SLF4J + Logback) — security critical
3. **Third**: Spring Framework upgrade (4.x → 6.x via Spring Boot 3.x)
4. **Fourth**: Hibernate upgrade (4.x → 6.x with JPA annotations)
5. **Fifth**: All other dependency updates
6. **Ongoing**: Architectural refactoring (service locator → DI, XML → annotations)

## Related Documentation

- [Outdated Components](outdated-components.md)
- [Maintenance Burden](maintenance-burden.md)
- [Remediation Plan](remediation-plan.md)
- [Technical Debt Report (Root)](../technical-debt-report.md)
