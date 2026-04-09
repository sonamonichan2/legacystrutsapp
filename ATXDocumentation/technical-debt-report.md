# Technical Debt Report — OpenMRS Core 2.0.3

## 🎯 AWS Transformation Recommendation

### **RECOMMENDED TRANSFORMATIONS: AWS/java-version-upgrade, AWS/early-access-log4j-to-slf4j-migration**

OpenMRS Core 2.0.3 is a Java 8 application with a target upgrade path to Java 21. The `AWS/java-version-upgrade` transformation is directly applicable to address the most critical technical debt item — upgrading the JDK version from 8 to 21 with comprehensive dependency modernization including Jakarta EE migration, database driver updates, ORM framework updates, and Spring ecosystem updates. Additionally, the `AWS/early-access-log4j-to-slf4j-migration` transformation addresses the EOL Log4j 1.2.15 dependency by migrating to SLF4J with Logback, which is a critical security and maintenance concern.

---

## Executive Summary

OpenMRS Core 2.0.3 carries **significant technical debt** across all layers. The codebase was last updated circa 2016-2017 and relies on frameworks and libraries that have since reached end-of-life. The most critical finding is the reliance on **Java 8, Spring Framework 4.1.4 (EOL), Hibernate 4.3.9 (EOL), and Log4j 1.2.15 (EOL with security concerns)**.

### Critical Statistics
- **6 HIGH severity** findings (EOL runtimes/frameworks)
- **25+ MEDIUM severity** findings (outdated runtime dependencies)
- **15+ LOW severity** findings (outdated dev/build tools)
- **5 architectural** technical debt items

## Priority Summary

### 🔴 HIGH — EOL/Deprecated Runtimes and Frameworks

| # | Component | Current Version | Status | Impact |
|---|-----------|----------------|--------|--------|
| 1 | **Java** | 1.8 | EOL (free Oracle JDK) | No modern language features, security patches ending |
| 2 | **Spring Framework** | 4.1.4.RELEASE | EOL (Dec 2020) | No security patches, blocks Spring Boot adoption |
| 3 | **Hibernate** | 4.3.9.Final | EOL | No patches, blocks JPA 3.x/Jakarta adoption |
| 4 | **Servlet API** | 3.0.1 (javax) | Outdated | Blocks Jakarta EE migration |
| 5 | **Log4j** | 1.2.15 | EOL (Aug 2015) | Security risk, no patches |
| 6 | **Liquibase** | 2.0.5 | Very outdated | Missing features, potential migration issues |

### 🟡 MEDIUM — Outdated Runtime Dependencies

25+ libraries with known outdated versions including Jackson (Codehaus EOL), XStream (security vulnerabilities), Commons libraries, database drivers, HAPI, Velocity, Ehcache, Lucene, and more.

### 🔵 LOW — Outdated Dev/Build Dependencies

15+ build plugins and test libraries including Maven Compiler Plugin 2.1, JUnit 4.11, Mockito 1.9.5, PowerMock 1.5.

### ⚙️ Architectural Technical Debt

| Issue | Description |
|-------|-------------|
| Service Locator Pattern | `Context.java` with ~308 `getService()` calls across service implementations |
| Flat Package Structure | No domain module boundaries in `org.openmrs.*` |
| XML Configuration | 689-line Spring XML + 84 Hibernate XML mappings |
| Hibernate XML Mappings | All entities use `.hbm.xml` instead of JPA annotations |
| Cyclic Service Dependencies | 5+ bidirectional service-to-service dependencies via Context |

## Navigation

- [Detailed Summary](technical-debt/summary.md)
- [Outdated Components (Full List)](technical-debt/outdated-components.md)
- [Maintenance Burden](technical-debt/maintenance-burden.md)
- [Remediation Plan](technical-debt/remediation-plan.md)
- [Analysis: Tech Debt](analysis/tech-debt.md)
- [Architecture: Dependencies](architecture/dependencies.md)
