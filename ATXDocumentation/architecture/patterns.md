# Architectural Patterns — OpenMRS Core 2.0.3

## Identified Patterns

### 1. Layered Architecture (Primary Pattern)

The application follows a strict four-layer architecture:

```
Web Layer (Controllers, Filters, Servlets)
    ↓
Service Layer (Business Logic, Transaction Boundaries)
    ↓
DAO Layer (Data Access, Hibernate Queries)
    ↓
Domain Model (Entities, Value Objects)
```

**Observations**:
- Layers are enforced by Maven module boundaries (web → api)
- The Service layer serves as the transaction boundary (Spring `@Transactional`)
- DAO layer uses Hibernate `SessionFactory` directly (no JPA `EntityManager`)

### 2. Service-DAO Pattern

Every domain aggregate follows a consistent pattern:
- `XxxService` (interface in `org.openmrs.api`)
- `XxxServiceImpl` (implementation in `org.openmrs.api.impl`)
- `XxxDAO` (DAO interface in `org.openmrs.api.db`)
- `HibernateXxxDAO` (Hibernate implementation in `org.openmrs.api.db.hibernate`)

**Examples**: PatientService → PatientServiceImpl → PatientDAO → HibernatePatientDAO

### 3. Service Locator Pattern (Anti-Pattern)

`Context.java` acts as a centralized service locator:
```java
Context.getPatientService()
Context.getConceptService()
Context.getEncounterService()
// etc.
```

**Issues**:
- Hides dependencies — classes do not declare their service dependencies explicitly
- Makes unit testing difficult without the full Spring context
- Creates a single point of coupling for all service access
- `ServiceContext.java` manages the underlying `ApplicationContext` and service registration

### 4. AOP-Based Cross-Cutting Concerns

Spring AOP is used extensively for:

| Advice Class | Purpose |
|-------------|---------|
| `AuthorizationAdvice` | Checks `@Authorized` annotation for privilege enforcement |
| `LoggingAdvice` | Logs method entry/exit for all service methods |
| `RequiredDataAdvice` | Auto-populates audit fields (creator, dateCreated, changedBy, etc.) |

The AOP proxy chain wraps every service method: Authorization → Logging → RequiredData → Actual method.

### 5. Template Method Pattern (Domain Entities)

Base classes provide common infrastructure:
```
OpenmrsObject (interface) → BaseOpenmrsObject (UUID, equals/hashCode)
  ├── OpenmrsData (interface) → BaseOpenmrsData (creator, dateCreated, voided, voidReason)
  │     └── BaseCustomizableData (attributes collection)
  └── OpenmrsMetadata (interface) → BaseOpenmrsMetadata (name, description, retired)
        └── BaseCustomizableMetadata (attributes collection)
```

### 6. Strategy Pattern (Dosing Instructions)

Order dosing instructions use the Strategy pattern:
- `DosingInstructions` (interface)
- `SimpleDosingInstructions` (structured dosing)
- `FreeTextDosingInstructions` (free-text dosing)

### 7. Observer Pattern (Event Listeners)

- `GlobalPropertyListener` — notified on global property changes
- `PrivilegeListener` — notified on privilege checks
- `EventListeners` — manages lists of registered listeners
- `ObsPostLoadEventListener` — Hibernate post-load events for Obs

### 8. Plugin/Module Architecture

A custom module system provides runtime extensibility:
- `ModuleFactory` — manages module lifecycle (loading, starting, stopping)
- `ModuleClassLoader` — custom classloader per module for isolation
- `ModuleFileParser` — parses `config.xml` from `.omod` files
- Extension points defined in Spring XML contexts (`moduleApplicationContext.xml`)

### 9. Filter Chain Pattern (Web Layer)

The web module implements a filter chain for request processing:
```
OpenmrsFilter → StartupFilter → InitializationFilter → UpdateFilter → GZIPFilter
```
- `StartupFilter` — redirects to initialization/update if not configured
- `InitializationFilter` — handles first-time setup wizard
- `UpdateFilter` — handles database schema updates
- `GZIPFilter` — response compression

### 10. Singleton Pattern (Context)

`Context` and `ServiceContext` are effective singletons managing application state:
- Thread-local `UserContext` for authentication state
- Static methods for service access
- Manages Hibernate `SessionFactory` lifecycle

## Anti-Patterns Identified

| Anti-Pattern | Location | Impact |
|-------------|----------|--------|
| **Service Locator** | `Context.java`, `ServiceContext.java` | Hidden dependencies, tight coupling |
| **God Class** | `Context.java` (~50+ static getService methods) | Central point of failure |
| **Flat Package Structure** | `org.openmrs.*` root package | No module boundaries |
| **XML Configuration Overuse** | `applicationContext-service.xml` (689 lines) | Hard to navigate, error-prone |
| **Hibernate XML Mappings** | 84 `.hbm.xml` files | Verbose, duplicates information from entities |
| **Mixed Concerns in API Module** | Domain, services, DAOs, HL7, scheduling all in `api` | Monolithic module |
| **Static Method Abuse** | `Context.authenticate()`, `Context.getService()` | Testing difficulties |

## Related Documentation

- [System Overview](system-overview.md)
- [Components](components.md)
- [Dependencies](dependencies.md)
- [Technical Debt Report](../technical-debt-report.md)
