# Dependency Analysis — OpenMRS Core 2.0.3

## Internal Cross-Module Coupling Analysis

### Service-to-Service Coupling via Context.getService()

The `Context` service locator pattern creates extensive hidden coupling between service implementations. Below are the `Context.get*()` call counts per service implementation:

| Service Implementation | Context.get* Calls | Coupled Services |
|-----------------------|-------------------|-----------------|
| EncounterServiceImpl | 48 | ObsService, OrderService, VisitService, LocationService, etc. |
| PatientServiceImpl | 48 | PersonService, ConceptService, UserService, ObsService, EncounterService, OrderService |
| ConceptServiceImpl | 44 | AdministrationService, ObsService, FormService |
| PersonServiceImpl | 31 | PatientService, UserService, AdministrationService |
| AdministrationServiceImpl | 28 | ConceptService, UserService, many others |
| FormServiceImpl | 21 | ConceptService, EncounterService |
| LocationServiceImpl | 16 | AdministrationService |
| OrderServiceImpl | 15 | ConceptService, PatientService, EncounterService, ProviderService |
| UserServiceImpl | 13 | PersonService, AdministrationService |
| ObsServiceImpl | 11 | ConceptService, EncounterService |
| ProgramWorkflowServiceImpl | 10 | ConceptService, PatientService |
| VisitServiceImpl | 8 | EncounterService, AdministrationService |
| ProviderServiceImpl | 7 | PersonService |
| CohortServiceImpl | 4 | PatientService |

**Total Context.get*() calls in service implementations: ~308**

### Cross-Domain Entity References

Analysis of import statements reveals tight coupling between domain entities:

**OrderServiceImpl** references (outside Order domain):
- `Concept`, `ConceptClass`, `Drug` (concept domain)
- `Patient` (patient domain)
- `Encounter` (encounter domain)
- `Provider` (provider domain)
- `User` (user domain)
- `GlobalProperty` (admin domain)

**EncounterServiceImpl** references (outside Encounter domain):
- `Patient` (patient domain)
- `Obs`, `ObsService` (observation domain)
- `Order`, `OrderGroup`, `OrderService` (order domain)
- `Location` (location domain)
- `Provider` (provider domain)
- `Visit`, `VisitType` (visit domain)
- `Cohort` (cohort domain)
- `Form` (form domain)
- `User`, `Privilege` (user domain)

## Cyclic Dependency Detection

### Entity-Level Cycles

| Cycle | Description |
|-------|-------------|
| **Person ↔ Patient** | `Patient extends Person`, but `Person` has references that may resolve to `Patient` instances (shared table inheritance) |
| **Encounter ↔ Obs** | `Encounter` contains `Set<Obs>`, `Obs` references back to `Encounter` |
| **Encounter ↔ Order** | `Encounter` contains `Set<Order>`, `Order` references `Encounter` |
| **Concept ↔ Drug** | `Drug` references `Concept` (dosageForm, route), `Concept` may have drug answers |
| **Order ↔ Concept** | `Order.concept` (what is ordered), `Concept` has no direct back-ref but ConceptClass relates |
| **Obs ↔ Concept** | `Obs.concept` and `Obs.valueCoded` reference `Concept` |
| **Visit ↔ Encounter** | `Visit` has `Set<Encounter>`, `Encounter` references `Visit` |

### Service-Level Cycles (via Context.getService())

| Cycle | Path |
|-------|------|
| **Patient ↔ Person** | PatientServiceImpl → Context.getPersonService(); PersonServiceImpl → Context.getPatientService() |
| **Patient ↔ Encounter** | PatientServiceImpl → Context.getEncounterService(); EncounterServiceImpl depends on Patient |
| **Encounter ↔ Obs** | EncounterServiceImpl → Context.getObsService(); ObsServiceImpl depends on Encounter |
| **Encounter ↔ Order** | EncounterServiceImpl → Context.getOrderService(); OrderServiceImpl depends on Encounter |
| **Encounter ↔ Visit** | EncounterServiceImpl → Context.getVisitService(); VisitServiceImpl → Context.getEncounterService() |

### Root Cause
The `Context` service locator creates hidden circular dependencies that are not detectable at compile time. In a modular-monolith architecture, these cycles must be broken using:
1. **Domain events** for notifications across modules
2. **Interface segregation** at module boundaries
3. **Dependency inversion** with interfaces in shared-kernel

## Mapping Current Packages to Proposed Target Modules

### shared-kernel
**Current Package** → **Target**
- `org.openmrs.BaseOpenmrsObject` → `shared-kernel/domain`
- `org.openmrs.BaseOpenmrsData` → `shared-kernel/domain`
- `org.openmrs.BaseOpenmrsMetadata` → `shared-kernel/domain`
- `org.openmrs.OpenmrsObject`, `OpenmrsData`, `OpenmrsMetadata` → `shared-kernel/domain`
- `org.openmrs.Auditable`, `Voidable`, `Retireable` → `shared-kernel/domain`
- `org.openmrs.BaseCustomizableData`, `BaseCustomizableMetadata` → `shared-kernel/domain`
- `org.openmrs.api.OpenmrsService` → `shared-kernel/application`
- `org.openmrs.api.APIException` (and subclasses) → `shared-kernel/domain`
- `org.openmrs.GlobalProperty` → `shared-kernel/domain`
- `org.openmrs.util.*` (core utilities) → `shared-kernel/util`

### person
- `org.openmrs.Person` → `person/domain`
- `org.openmrs.PersonName`, `PersonAddress`, `PersonAttribute`, `PersonAttributeType` → `person/domain`
- `org.openmrs.Relationship`, `RelationshipType` → `person/domain`
- `org.openmrs.api.PersonService` → `person/application`
- `org.openmrs.api.impl.PersonServiceImpl` → `person/application`
- `org.openmrs.api.db.PersonDAO`, `HibernatePersonDAO` → `person/infrastructure`

### patient
- `org.openmrs.Patient` → `patient/domain`
- `org.openmrs.PatientIdentifier`, `PatientIdentifierType` → `patient/domain`
- `org.openmrs.Allergy*` → `patient/domain`
- `org.openmrs.api.PatientService` → `patient/application`
- `org.openmrs.api.impl.PatientServiceImpl` → `patient/application`
- `org.openmrs.api.db.PatientDAO`, `HibernatePatientDAO` → `patient/infrastructure`

### concepts
- `org.openmrs.Concept*` (all ~20 concept classes) → `concepts/domain`
- `org.openmrs.Drug*`, `ConceptMap*`, `ConceptSource` → `concepts/domain`
- `org.openmrs.api.ConceptService` → `concepts/application`
- `org.openmrs.api.impl.ConceptServiceImpl` → `concepts/application`
- `org.openmrs.api.db.ConceptDAO`, `HibernateConceptDAO` → `concepts/infrastructure`

### encounter
- `org.openmrs.Encounter*` → `encounter/domain`
- `org.openmrs.api.EncounterService` → `encounter/application`
- Dependencies: patient, person, concepts, location, provider

### observation
- `org.openmrs.Obs`, `org.openmrs.obs.*` → `observation/domain`
- `org.openmrs.api.ObsService` → `observation/application`
- Dependencies: encounter, concepts, patient, person

### orders
- `org.openmrs.Order`, `DrugOrder`, `TestOrder`, `OrderType`, `OrderFrequency`, `OrderGroup`, `OrderSet*`, `CareSetting` → `orders/domain`
- `org.openmrs.api.OrderService`, `OrderSetService` → `orders/application`
- Dependencies: concepts, patient, encounter, provider

### users-admin
- `org.openmrs.User`, `Role`, `Privilege` → `users-admin/domain`
- `org.openmrs.api.UserService`, `AdministrationService` → `users-admin/application`
- Dependencies: person, shared-kernel

### location
- `org.openmrs.Location`, `LocationTag`, `LocationAttribute`, `LocationAttributeType` → `location/domain`
- `org.openmrs.api.LocationService` → `location/application`
- Dependencies: shared-kernel only

### integration
- `org.openmrs.hl7.*` → `integration/hl7`
- `org.openmrs.module.*` → `integration/module`
- `org.openmrs.serialization.*` → `integration/serialization`
- `org.openmrs.notification.*` → `integration/notification`

### search-reporting
- Hibernate Search integration classes → `search-reporting/search`
- `org.openmrs.Cohort`, `CohortService` → `search-reporting/cohort`

### web-adapter
- `org.openmrs.web.*` → `web-adapter/`
- `org.openmrs.module.web.*` → `web-adapter/module`
- Dependencies: all domain modules

## Coupling Severity Assessment

| Coupling Type | Count | Severity | Migration Impact |
|--------------|-------|----------|-----------------|
| Context.getService() calls | ~308 | High | Must replace with DI |
| Entity cross-references | ~30+ bidirectional | High | Requires interface extraction |
| Service-to-Service cycles | ~5 cycles | High | Needs domain events |
| Flat package structure | All in org.openmrs | Medium | Package moves needed |
| Hibernate XML mappings | 84 files | Medium | Must migrate with entities |
| Spring XML config | 689 lines | Medium | Convert to Java config |

## Related Documentation

- [Dependencies](../architecture/dependencies.md)
- [Components](../architecture/components.md)
- [Migration Plan](../migration/component-order.md)
- [Technical Debt Report](../technical-debt-report.md)
