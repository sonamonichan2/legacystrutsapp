# Specialized Documentation — OpenMRS Core 2.0.3

## Database Schemas

### Hibernate Entity Mappings (84 total)

All entity-to-database mappings are defined in `.hbm.xml` files under `api/src/main/resources/org/openmrs/api/db/hibernate/`.

**Core Domain Tables (~60 mappings)**:
- Patient/Person: `Person`, `PersonName`, `PersonAddress`, `PersonAttribute`, `PersonAttributeType`, `PersonMergeLog`, `Patient`, `PatientIdentifier`, `PatientIdentifierType`, `Relationship`, `RelationshipType`
- Encounter: `Encounter`, `EncounterType`, `EncounterProvider`, `EncounterRole`
- Observation: `Obs`
- Order: `Order`, `OrderType`, `OrderSet`, `OrderSetMember`, `OrderGroup`, `OrderFrequency`, `CareSetting`
- Concept: `Concept`, `ConceptAnswer`, `ConceptAttribute`, `ConceptAttributeType`, `ConceptDescription`, `ConceptName`, `ConceptNameTag`, `ConceptClass`, `ConceptDatatype`, `ConceptProposal`, `ConceptStateConversion`, `ConceptSet`, `ConceptMap`, `ConceptStopWord`, `ConceptSource`, `ConceptReferenceTerm`, `ConceptMapType`, `ConceptReferenceTermMap`
- Drug: `Drug`, `DrugIngredient`, `DrugReferenceMap`
- Form: `Form`, `FormField`, `FormResource`, `Field`, `FieldAnswer`, `FieldType`
- Location: `Location`, `LocationTag`, `LocationAttributeType`, `LocationAttribute`
- Visit: `Visit`, `VisitType`, `VisitAttributeType`, `VisitAttribute`
- Provider: `Provider`, `ProviderAttribute`, `ProviderAttributeType`
- User/Security: `User`, `LoginCredential`, `Privilege`, `Role`
- Program: `Program`, `ProgramWorkflow`, `ProgramWorkflowState`, `PatientProgram`, `PatientState`
- Other: `GlobalProperty`, `SerializedObject`, `Cohort`, `Allergy`, `AllergyReaction`, `ClobDatatypeStorage`

**HL7 Tables (4 mappings)**: `HL7Source`, `HL7InQueue`, `HL7InArchive`, `HL7InError`
**Notification Tables (3 mappings)**: `Template`, `Alert`, `AlertRecipient`
**Scheduler Tables (1 mapping)**: `TaskDefinition`

### Liquibase Migration Files

| File | Purpose | Location |
|------|---------|----------|
| `liquibase-schema-only.xml` | DDL schema creation | `api/src/main/resources/` |
| `liquibase-update-to-latest.xml` | Incremental schema updates | `api/src/main/resources/` |
| `liquibase-core-data.xml` | Core reference data (roles, privileges, etc.) | `api/src/main/resources/` |
| `liquibase-demo-data.zip` | Demo/sample data | `webapp/src/main/resources/` |

### Database Drivers

| Database | Driver | Version | JDBC URL Pattern |
|----------|--------|---------|-----------------|
| MySQL | mysql:mysql-connector-java | 5.1.28 | `jdbc:mysql://host:3306/openmrs` |
| PostgreSQL | postgresql:postgresql | 9.0-801.jdbc4 | `jdbc:postgresql://host:5432/openmrs` |
| H2 (test) | com.h2database:h2 | 1.4.187 | `jdbc:h2:mem:openmrs` |

---

## HL7 Integration

### Overview
OpenMRS processes HL7v2 messages using the HAPI library (v2.0). Supported message types:
- **ADT^A28**: Patient registration/update
- **ORU^R01**: Observation results (lab results, vitals)

### Components
- `HL7Service` — service interface for HL7 message management
- `HL7ServiceImpl` — implementation
- `HL7InQueueProcessor` — processes messages from the HL7 inbound queue
- `ADTA28Handler` — handles ADT^A28 messages (patient registration)
- `ORUR01Handler` — handles ORU^R01 messages (observation results)
- `HL7InQueue` — inbound message queue (database table)
- `HL7InArchive` — successfully processed messages
- `HL7InError` — failed message processing

### Processing Flow
1. External system sends HL7 message → stored in `HL7InQueue`
2. `ProcessHL7InQueueTask` (scheduled) picks up messages
3. Message parsed by HAPI, routed to appropriate handler
4. Handler creates/updates domain objects (Patient, Encounter, Obs)
5. Success → `HL7InArchive`; Failure → `HL7InError`

---

## Module/Plugin System

### Overview
OpenMRS has a custom module system that allows third-party extensions (`.omod` files) to be dynamically loaded at runtime.

### Key Components
- `ModuleFactory` (1,687 lines) — central module lifecycle manager
- `ModuleClassLoader` (1,103 lines) — per-module classloader for isolation
- `ModuleFileParser` — parses `config.xml` from `.omod` archives
- `Module` — metadata container (name, version, dependencies, extension points)
- `ModuleActivator` — lifecycle callback interface (willStart, started, willStop, stopped)
- `WebModuleUtil` — web integration (registers module servlets, filters)

### Module Lifecycle
1. **Load**: Parse `.omod` file, create `ModuleClassLoader`, read `config.xml`
2. **Start**: Load module Spring context, register services, call `ModuleActivator.started()`
3. **Running**: Module services available via `Context`, web extensions active
4. **Stop**: Call `ModuleActivator.willStop()`, unregister services, destroy classloader

### Extension Points
- Spring context files: `moduleApplicationContext.xml`, `webModuleApplicationContext.xml`
- AOP advice: modules can register custom AOP advice via `AdvicePoint`
- DWR: modules can expose JavaScript services
- Web filters/servlets: registered via `WebModuleUtil`

---

## Scheduler System

### Overview
Timer-based task scheduling for background operations.

### Components
- `SchedulerService` — service interface
- `TimerSchedulerServiceImpl` — implementation using `java.util.Timer`
- `TaskDefinition` — persisted task configuration
- `Task` / `StatefulTask` — task execution interfaces
- `AbstractTask` — base implementation

### Built-in Tasks
| Task | Purpose |
|------|---------|
| `ProcessHL7InQueueTask` | Processes HL7 messages from inbound queue |
| `AutoCloseVisitsTask` | Auto-closes open visits past threshold |
| `AlertReminderTask` | Sends alert reminders |
| `CheckInternetConnectivityTask` | Checks internet connectivity |
| `SendEmailTask` | Sends email notifications |

---

## Related Documentation

- [System Overview](../architecture/system-overview.md)
- [Components](../architecture/components.md)
- [Data Models](../reference/data-models.md)
- [Interfaces](../reference/interfaces.md)
