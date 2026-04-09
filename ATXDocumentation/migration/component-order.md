# Component Migration Order — OpenMRS Core 2.0.3 → Modular Monolith

## Migration Strategy Overview

The migration follows a bottom-up approach, extracting modules from the least dependent to the most dependent. Each wave produces a buildable, deployable application.

**WAR-first vs Spring Boot timing**: Start with WAR packaging (Waves 1-3) to minimize risk, introduce Spring Boot starter in Wave 4, complete Spring Boot transition in Wave 6.

## Module Migration Order

| Order | Module | Dependencies | Wave |
|-------|--------|-------------|------|
| 1 | **shared-kernel** | None | Wave 1 |
| 2 | **location** | shared-kernel | Wave 1 |
| 3 | **person** | shared-kernel | Wave 1 |
| 4 | **users-admin** | person, shared-kernel | Wave 2 |
| 5 | **concepts** | shared-kernel | Wave 2 |
| 6 | **patient** | person, shared-kernel | Wave 3 |
| 7 | **encounter** | patient, person, concepts, location | Wave 3 |
| 8 | **observation** | encounter, concepts, patient, person | Wave 4 |
| 9 | **orders** | concepts, patient, encounter | Wave 4 |
| 10 | **search-reporting** | patient, concepts, shared-kernel | Wave 5 |
| 11 | **integration** | shared-kernel, multiple domain modules | Wave 5 |
| 12 | **web-adapter** | all domain modules | Wave 6 |

## Wave Definitions

### Wave 1: Foundation (shared-kernel, location, person)

**Goal**: Extract the foundation modules with zero downstream dependencies.

**shared-kernel** extraction:
- Move `BaseOpenmrsObject`, `BaseOpenmrsData`, `BaseOpenmrsMetadata` → `org.openmrs.kernel.domain`
- Move `OpenmrsObject`, `OpenmrsData`, `OpenmrsMetadata` interfaces → `org.openmrs.kernel.domain`
- Move `Auditable`, `Voidable`, `Retireable` interfaces → `org.openmrs.kernel.domain`
- Move `BaseCustomizableData`, `BaseCustomizableMetadata` → `org.openmrs.kernel.domain`
- Move `APIException` and common exceptions → `org.openmrs.kernel.exception`
- Move `OpenmrsService` → `org.openmrs.kernel.application`
- Move core utilities (`OpenmrsUtil`, `Security`) → `org.openmrs.kernel.util`
- Move `GlobalProperty` → `org.openmrs.kernel.domain`
- Create Maven module: `openmrs-shared-kernel`

**location** extraction:
- Move `Location`, `LocationTag`, `LocationAttribute`, `LocationAttributeType` → `org.openmrs.location.domain`
- Move `LocationService` → `org.openmrs.location.application`
- Move `LocationServiceImpl` → `org.openmrs.location.application`
- Move `LocationDAO`, `HibernateLocationDAO` → `org.openmrs.location.infrastructure`
- Move location validators → `org.openmrs.location.application`
- Move location HBM XMLs → within module resources
- Create Maven module: `openmrs-location`

**person** extraction:
- Move `Person`, `PersonName`, `PersonAddress`, `PersonAttribute`, `PersonAttributeType` → `org.openmrs.person.domain`
- Move `Relationship`, `RelationshipType` → `org.openmrs.person.domain`
- Move `PersonService` → `org.openmrs.person.application`
- Move validators and DAO classes → within module
- Create Maven module: `openmrs-person`

**Deployment**: WAR | **Java**: 8 (upgrade to 21 can happen here)

### Wave 2: Independent Domains (users-admin, concepts)

**users-admin** extraction:
- Move `User`, `Role`, `Privilege` → `org.openmrs.security.domain`
- Move `UserService`, `AdministrationService` → `org.openmrs.security.application`
- Move validators and DAOs → within module
- Extract authentication logic from `Context` into a dedicated `AuthenticationService`
- Create Maven module: `openmrs-users-admin`

**concepts** extraction:
- Move all `Concept*` classes (~20) → `org.openmrs.concepts.domain`
- Move `Drug`, `DrugIngredient`, `DrugReferenceMap` → `org.openmrs.concepts.domain`
- Move `ConceptService` → `org.openmrs.concepts.application`
- Move `HibernateConceptDAO` → `org.openmrs.concepts.infrastructure`
- Create Maven module: `openmrs-concepts`

**Deployment**: WAR

### Wave 3: Core Clinical (patient, encounter)

**patient** extraction:
- Move `Patient`, `PatientIdentifier`, `PatientIdentifierType` → `org.openmrs.patient.domain`
- Move `Allergy*` classes → `org.openmrs.patient.domain`
- Move `PatientService` → `org.openmrs.patient.application`
- **Key challenge**: Break Patient↔Person merge coupling
  - Define `PersonMergePort` interface in shared-kernel
  - PatientServiceImpl delegates merge to PersonService via interface
- Create Maven module: `openmrs-patient`

**encounter** extraction:
- Move `Encounter*` classes → `org.openmrs.encounter.domain`
- Move `EncounterService` → `org.openmrs.encounter.application`
- **Key challenge**: Encounter cascades obs and orders on save
  - Extract obs/order save into event-driven pattern
  - Use domain events: `ObsSaveRequested`, `OrderSaveRequested`
- Create Maven module: `openmrs-encounter`

**Deployment**: WAR

### Wave 4: Clinical Data (observation, orders) + Spring Boot Introduction

**observation** extraction:
- Move `Obs`, complex obs handler → `org.openmrs.observation.domain`
- Move `ObsService` → `org.openmrs.observation.application`
- Create Maven module: `openmrs-observation`

**orders** extraction:
- Move `Order`, `DrugOrder`, `TestOrder`, `OrderType`, `OrderFrequency`, `OrderGroup`, `OrderSet*`, `CareSetting` → `org.openmrs.orders.domain`
- Move `OrderService`, `OrderSetService` → `org.openmrs.orders.application`
- Move `DosingInstructions` strategy classes → `org.openmrs.orders.domain`
- Create Maven module: `openmrs-orders`

**Spring Boot introduction**:
- Add `spring-boot-starter-parent` or `spring-boot-dependencies` BOM
- Create `@SpringBootApplication` main class
- Convert `applicationContext-service.xml` to `@Configuration` classes
- Retain WAR packaging capability (`SpringBootServletInitializer`)

**Deployment**: WAR (Spring Boot executable WAR)

### Wave 5: Supporting Domains (search-reporting, integration)

**search-reporting** extraction:
- Move `Cohort`, `CohortService` → `org.openmrs.reporting.domain`
- Move Hibernate Search integration → `org.openmrs.search.infrastructure`
- Create Maven module: `openmrs-search-reporting`

**integration** extraction:
- Move HL7 package → `org.openmrs.integration.hl7`
- Move module system → `org.openmrs.integration.module`
- Move serialization → `org.openmrs.integration.serialization`
- Move notification → `org.openmrs.integration.notification`
- Create Maven module: `openmrs-integration`

**Deployment**: Spring Boot WAR/JAR

### Wave 6: Web Adapter + Full Spring Boot (final)

**web-adapter** extraction:
- Move all web filters, controllers, servlets → `org.openmrs.web`
- Replace `web.xml` with Spring Boot auto-configuration
- Replace filter chain with Spring Security filter chain
- Remove Jetty/Tomcat Maven plugins (use Spring Boot embedded)
- Create Maven module: `openmrs-web-adapter`

**Full Spring Boot transition**:
- Remove WAR packaging, switch to executable JAR
- Embedded Tomcat via Spring Boot
- Spring Boot auto-configuration for Hibernate, Liquibase, etc.
- Remove `applicationContext-service.xml` completely

**Deployment**: Spring Boot executable JAR (or WAR for backward compat)

## Interface Extraction Plan

### Cross-Module Interfaces (in shared-kernel)

```java
// Patient → Person bridge
public interface PersonLookupPort {
    Person getPersonById(Integer id);
    Person getPersonByUuid(String uuid);
}

// Encounter → ObsService bridge
public interface ObservationSavePort {
    Obs saveObs(Obs obs, String changeMessage);
}

// Encounter → OrderService bridge
public interface OrderSavePort {
    Order saveOrder(Order order);
}

// General service event
public interface DomainEvent<T> {
    T getPayload();
    Date getTimestamp();
}
```

### Replacing Context.getService()

For each `Context.getXxxService()` call:
1. Add constructor parameter for the needed service interface
2. Use Spring `@Autowired` or `@Inject`
3. In shared-kernel, define cross-module port interfaces
4. Each module implements its own ports

## Backward Compatibility Risks

| Risk | Impact | Mitigation |
|------|--------|-----------|
| Module API Changes | High — ~200+ community modules depend on `Context.getService()` | Maintain `Context` facade with deprecation warnings; provide adapter for existing modules |
| Hibernate Session Sharing | High — modules share Hibernate session | Keep single `SessionFactory` in Wave 1-3; migrate to per-module config in Wave 5-6 |
| Package Moves | Medium — module code references `org.openmrs.*` packages | Provide re-export packages or shim classes during transition |
| Liquibase Migration | Medium — must maintain continuous migration path | Keep all Liquibase files in shared location; add new changelogs per module |
| Spring Bean Names | Low — modules may reference beans by name | Maintain bean aliases during transition |

## Rollback Strategy

| Wave | Rollback Approach |
|------|------------------|
| Wave 1 | Revert Maven module structure; keep flat package layout |
| Wave 2 | Merge extracted modules back into api module |
| Wave 3 | Revert patient/encounter extraction; restore direct coupling |
| Wave 4 | Remove Spring Boot; revert to pure Spring XML config |
| Wave 5 | Merge integration/reporting back into api module |
| Wave 6 | Revert to WAR packaging with web.xml |

**General rollback principles**:
- Each wave's git branch is preserved for rollback
- Database schema changes are backward-compatible (add-only, no destructive changes)
- Module API compatibility is maintained throughout transition
- Deployment can revert to previous WAR artifact

## WAR-first vs Spring Boot Transition Timing

**Recommendation**: WAR-first for Waves 1-3, Spring Boot introduction in Wave 4, full transition in Wave 6.

**Rationale**:
1. WAR-first preserves existing deployment pipeline on AWS EC2
2. Allows module restructuring without framework migration risk
3. Spring Boot introduction in Wave 4 provides immediate benefits (auto-configuration, actuator)
4. Full Spring Boot in Wave 6 enables embedded server, executable JAR
5. Intermediate deployability on EC2 maintained throughout

## Related Documentation

- [Test Specifications](test-specifications.md)
- [Validation Criteria](validation-criteria.md)
- [Dependency Analysis](../analysis/dependency-analysis.md)
- [Proposed Architecture](../diagrams/architecture/architecture-diagrams.md)
