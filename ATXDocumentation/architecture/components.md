# Components — OpenMRS Core 2.0.3

## Maven Module Components

### 1. `tools` Module (org.openmrs.tools)
- **Packaging**: JAR
- **Purpose**: Build-time tooling — checkstyle configs, code formatters, custom Javadoc taglets
- **Key Classes**: `ShouldTaglet` (Javadoc taglet)
- **Dependencies**: JDK tools.jar (profile-activated)

### 2. `test` Module (org.openmrs.test)
- **Packaging**: POM (dependency aggregator)
- **Purpose**: Aggregates all test dependencies for reuse across modules
- **Dependencies**: JUnit 4.11, Mockito 1.9.5, PowerMock 1.5, Spring Test, H2 1.4.187, DBUnit 2.4.7, XMLUnit 1.3, Hamcrest 1.3, Databene Benerator

### 3. `api` Module (org.openmrs.api) — **Core Module**
- **Packaging**: JAR
- **Purpose**: Domain model, service interfaces, service implementations, DAOs, business logic, HL7 processing, scheduling, module system, validation
- **Source Files**: ~649 main Java files, ~288 test files
- **Key Packages**:

| Package | Description | File Count |
|---------|-------------|-----------|
| `org.openmrs` | Domain entity classes | ~105 |
| `org.openmrs.api` | Service interfaces + exceptions | ~52 |
| `org.openmrs.api.impl` | Service implementations | ~20 |
| `org.openmrs.api.db` | DAO interfaces | ~27 |
| `org.openmrs.api.db.hibernate` | Hibernate DAO implementations + interceptors | ~44 |
| `org.openmrs.api.context` | Context, ServiceContext, UserContext, Daemon | ~6 |
| `org.openmrs.validator` | Spring Validators | ~56 |
| `org.openmrs.module` | Module/plugin system | ~22 |
| `org.openmrs.hl7` | HL7 processing | ~15+ |
| `org.openmrs.scheduler` | Task scheduling | ~10+ |
| `org.openmrs.notification` | Alerts and notifications | ~10+ |
| `org.openmrs.aop` | AOP advice classes | 3 |
| `org.openmrs.util` | Utilities | ~30+ |
| `org.openmrs.serialization` | Serialization framework | ~5+ |
| `org.openmrs.customdatatype` | Custom datatype framework | ~15+ |
| `org.openmrs.attribute` | Attribute type framework | ~5+ |

### 4. `web` Module (org.openmrs.web)
- **Packaging**: JAR
- **Purpose**: Web layer — filters, controllers, servlet configuration, initialization wizard
- **Source Files**: 39 main Java files, 11 test files
- **Key Packages**:

| Package | Description |
|---------|-------------|
| `org.openmrs.web` | Core web classes: Listener, DispatcherServlet, StaticDispatcherServlet |
| `org.openmrs.web.filter` | Servlet filters: OpenmrsFilter, StartupFilter, GZIPFilter |
| `org.openmrs.web.filter.initialization` | First-time setup wizard |
| `org.openmrs.web.filter.update` | Database update handling |
| `org.openmrs.web.filter.startuperror` | Startup error display |
| `org.openmrs.web.controller` | PseudoStaticContentController |
| `org.openmrs.module.web` | Module web integration |

### 5. `webapp` Module (org.openmrs.web:openmrs-webapp)
- **Packaging**: WAR
- **Purpose**: Final deployable artifact — web.xml, JSP/static resources, Liquibase demo data
- **Dependencies**: api + web modules
- **Configuration**: web.xml (Servlet 3.0), Jetty/Tomcat plugins

---

## Service Layer Components

Each service follows the pattern: **Interface → Implementation → DAO Interface → Hibernate DAO**

| Service Interface | Implementation | DAO | Hibernate DAO | Domain |
|------------------|---------------|-----|---------------|--------|
| `PatientService` | `PatientServiceImpl` | `PatientDAO` | `HibernatePatientDAO` | Patient management, identifiers, merging |
| `PersonService` | `PersonServiceImpl` | `PersonDAO` | `HibernatePersonDAO` | Person demographics, attributes, names, addresses |
| `EncounterService` | `EncounterServiceImpl` | `EncounterDAO` | `HibernateEncounterDAO` | Clinical encounters, providers, roles |
| `ObsService` | `ObsServiceImpl` | `ObsDAO` | `HibernateObsDAO` | Clinical observations, complex obs |
| `OrderService` | `OrderServiceImpl` | `OrderDAO` | `HibernateOrderDAO` | Drug orders, test orders, order types |
| `OrderSetService` | `OrderSetServiceImpl` | `OrderSetDAO` | `HibernateOrderSetDAO` | Order set management |
| `ConceptService` | `ConceptServiceImpl` | `ConceptDAO` | `HibernateConceptDAO` | Medical concepts, drugs, mappings |
| `LocationService` | `LocationServiceImpl` | `LocationDAO` | `HibernateLocationDAO` | Facility locations, tags |
| `VisitService` | `VisitServiceImpl` | `VisitDAO` | `HibernateVisitDAO` | Patient visits, visit types |
| `ProviderService` | `ProviderServiceImpl` | `ProviderDAO` | `HibernateProviderDAO` | Healthcare providers |
| `UserService` | `UserServiceImpl` | `UserDAO` | `HibernateUserDAO` | User accounts, roles, privileges |
| `AdministrationService` | `AdministrationServiceImpl` | `AdministrationDAO` | `HibernateAdministrationDAO` | System config, global properties |
| `FormService` | `FormServiceImpl` | `FormDAO` | `HibernateFormDAO` | Clinical forms, fields |
| `CohortService` | `CohortServiceImpl` | `CohortDAO` | `HibernateCohortDAO` | Patient cohorts |
| `ProgramWorkflowService` | `ProgramWorkflowServiceImpl` | `ProgramWorkflowDAO` | `HibernateProgramWorkflowDAO` | Programs, workflows, states |
| `SerializationService` | `SerializationServiceImpl` | `SerializedObjectDAO` | `HibernateSerializedObjectDAO` | Object serialization |
| `DatatypeService` | `DatatypeServiceImpl` | `DatatypeDAO` | `HibernateDatatypeDAO` | Custom datatypes |

### Additional Services (without full DAO pattern)
- `HL7Service` — HL7 message processing (in `org.openmrs.hl7`)
- `SchedulerService` — Task scheduling (in `org.openmrs.scheduler`)
- `AlertService` — User notifications (in `org.openmrs.notification`)
- `MessageService` — Email notifications (in `org.openmrs.notification`)

---

## AOP Advice Components

| Component | Type | Purpose |
|-----------|------|---------|
| `AuthorizationAdvice` | `MethodBeforeAdvice` | Checks `@Authorized` annotations for required privileges |
| `LoggingAdvice` | `MethodInterceptor` | Logs method entry/exit with parameters and timing |
| `RequiredDataAdvice` | `MethodInterceptor` | Auto-populates audit fields (creator, changedBy, dateCreated, etc.) |

---

## Module System Components

| Component | Purpose |
|-----------|---------|
| `ModuleFactory` | Central factory for loading, starting, stopping modules |
| `ModuleClassLoader` | Custom classloader per module for class isolation |
| `ModuleFileParser` | Parses module config.xml from .omod files |
| `Module` | Module metadata container |
| `ModuleActivator` | Interface for module lifecycle callbacks |
| `BaseModuleActivator` | Base implementation with default no-op methods |
| `ModuleUtil` | Utility methods for module operations |
| `Extension` | Extension point mechanism |
| `AdvicePoint` | AOP advice registration for modules |
| `DaemonToken` / `DaemonTokenAware` | Security tokens for daemon threads |

---

## Web Layer Components

| Component | Purpose |
|-----------|---------|
| `Listener` | `ServletContextListener` — application startup/shutdown |
| `DispatcherServlet` | Custom Spring `DispatcherServlet` |
| `StaticDispatcherServlet` | Dispatches static content |
| `OpenmrsFilter` | Main request filter |
| `StartupFilter` | Redirects to init/update if needed |
| `InitializationFilter` | First-time setup wizard |
| `UpdateFilter` | Database schema update handler |
| `GZIPFilter` | Response compression |
| `PseudoStaticContentController` | Static content serving |
| `WebModuleUtil` | Module web integration utilities |

---

## Validator Components (56 validators)

Validators implement Spring's `Validator` interface for input validation:

**Patient Domain**: PatientValidator, PatientIdentifierValidator, PatientIdentifierTypeValidator, PatientProgramValidator
**Person Domain**: PersonValidator, PersonNameValidator, PersonAddressValidator, PersonAttributeTypeValidator, PersonMergeLogValidator
**Encounter Domain**: EncounterValidator, EncounterTypeValidator, EncounterRoleValidator
**Obs Domain**: ObsValidator
**Order Domain**: OrderValidator, DrugOrderValidator, TestOrderValidator, OrderTypeValidator, OrderFrequencyValidator
**Concept Domain**: ConceptValidator, ConceptClassValidator, ConceptDatatypeValidator, ConceptDrugValidator, ConceptMapTypeValidator, ConceptNameTagValidator, ConceptReferenceTermValidator, ConceptSourceValidator, ConceptAttributeTypeValidator, DrugValidator
**Location Domain**: LocationValidator, LocationTagValidator, LocationAttributeTypeValidator
**Visit Domain**: VisitValidator, VisitTypeValidator, VisitAttributeTypeValidator
**User/Security Domain**: UserValidator, RoleValidator, PrivilegeValidator
**Form Domain**: FormValidator, FieldValidator, FieldTypeValidator
**Other**: AlertValidator, AllergyValidator, HL7SourceValidator, ImplementationIdValidator, ProgramValidator, ProviderValidator, ProviderAttributeTypeValidator, RelationshipTypeValidator, RelationshipValidator, SchedulerFormValidator, StateConversionValidator
**Base Classes**: BaseAttributeTypeValidator, BaseAttributeValidator, BaseCustomizableValidator, RequireNameValidator, ValidateUtil

## Related Documentation

- [System Overview](system-overview.md)
- [Patterns](patterns.md)
- [Dependencies](dependencies.md)
- [Program Structure](../reference/program-structure.md)
- [Interfaces](../reference/interfaces.md)
- [Data Models](../reference/data-models.md)
