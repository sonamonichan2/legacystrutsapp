# Test Specifications — OpenMRS Core 2.0.3 Migration

## Testing Strategy Overview

Each migration wave requires three levels of testing:
1. **Unit Tests**: Module-level tests for extracted components
2. **Contract Tests**: Interface compliance at module boundaries
3. **Integration Tests**: Cross-module interactions via the shared-kernel interfaces

## Wave 1: Foundation Tests

### shared-kernel Tests
- Base class behavior: UUID generation, equals/hashCode contract on `BaseOpenmrsObject`
- Audit field handling: creator, dateCreated auto-population via `RequiredDataAdvice`
- Void/retire behavior: voided flag, voidedBy, voidReason chain
- GlobalProperty CRUD operations
- Utility class correctness (OpenmrsUtil, Security)

### location Module Tests
- Location CRUD: save, get, getByUuid, retire, unretire, purge
- Location hierarchy: parentLocation/childLocations navigation
- LocationTag management: save, get, associate with locations
- LocationAttribute management: save with custom datatypes
- LocationValidator: name required, no duplicate names

### person Module Tests
- Person CRUD: save, get, getByUuid, void, unvoid
- PersonName management: save, preferred name selection, format
- PersonAddress management: save, preferred address
- PersonAttribute management: save with various value types
- Relationship CRUD: save, get, bidirectional type labels
- PersonValidator: gender required, birthdate validation

## Wave 2: Independent Domain Tests

### users-admin Module Tests
- User CRUD: save, get, getByUsername, retire
- Password policy enforcement: min length, complexity, reuse prevention
- Role hierarchy: parent role inheritance, privilege aggregation
- Privilege management: save, get, assign to roles
- Authentication: authenticate → UserContext setup
- UserValidator: username format, system ID uniqueness

### concepts Module Tests
- Concept CRUD: save, get, getByName, getByMapping, retire
- ConceptName locale handling: fully specified, preferred, short names
- ConceptAnswer management: add/remove coded answers
- ConceptSet management: set membership
- ConceptMap/ConceptSource: external terminology mappings
- Drug CRUD: save, get, getDrugs by name
- ConceptValidator: name uniqueness per locale, datatype required

## Wave 3: Core Clinical Tests

### patient Module Tests
- Patient CRUD: save, get, getByUuid, void, unvoid, purge
- PatientIdentifier validation: format, check digit, uniqueness
- Patient search: by name, by identifier, by attributes
- Patient merging: merge two patients, verify data moved
- Allergy management: set allergies, "No Known Allergies"
- PatientValidator: at least one identifier, identifier validity

### encounter Module Tests
- Encounter CRUD: save with obs and orders, get, void
- EncounterProvider assignment: assign providers with roles
- Encounter search: by patient, by date range, by type
- Encounter transfer: move to different patient
- Visit association: link encounter to visit
- EncounterValidator: patient required, type required, datetime validation

## Wave 4: Clinical Data Tests + Spring Boot

### observation Module Tests
- Obs CRUD: save (immutable pattern), get, void
- Obs value types: numeric, coded, text, datetime, drug, complex
- Obs groups: create parent-child obs hierarchy
- Complex obs: file-based obs handling
- ObsValidator: concept-datatype match, numeric range validation

### orders Module Tests
- Order lifecycle: NEW → ACTIVE → REVISE/DISCONTINUE/EXPIRE
- DrugOrder specifics: dose, frequency, duration, auto-expiry calculation
- TestOrder specifics: laterality, specimen source
- Order validation: patient, concept, orderer required
- OrderSet management: create, modify, order set members
- CareSetting: inpatient vs outpatient

### Spring Boot Tests
- Application context loads successfully
- All services are available via DI (not Context.getService())
- Hibernate session factory initializes
- Liquibase migrations run on startup
- Actuator endpoints respond

## Wave 5: Supporting Domain Tests

### search-reporting Module Tests
- Cohort CRUD: save, get, add/remove members
- Hibernate Search: concept search by name (Lucene integration)
- Patient search via Lucene: name, identifier fulltext

### integration Module Tests
- HL7 message processing: ADT^A28, ORU^R01 handlers
- HL7 error handling: invalid messages → HL7InError
- Module lifecycle: load, start, stop module
- ModuleClassLoader: class isolation, resource loading
- Scheduler: task registration, execution, shutdown

## Wave 6: Web Adapter Tests

### web-adapter Module Tests
- Filter chain: request processing through OpenmrsFilter → GZIPFilter
- Startup flow: StartupFilter → InitializationFilter → UpdateFilter
- DispatcherServlet: request routing
- OWASP encoding: XSS prevention
- Module web integration: module servlets, filters

## Contract Test Specifications

### Cross-Module Contracts

| Contract | Producer | Consumer | Test |
|----------|----------|----------|------|
| PersonLookupPort | person module | patient module | Given personId, returns Person |
| ObservationSavePort | observation module | encounter module | Given Obs, saves and returns saved Obs |
| OrderSavePort | orders module | encounter module | Given Order, saves and returns saved Order |
| ConceptLookupPort | concepts module | observation, orders | Given conceptId, returns Concept |
| LocationLookupPort | location module | encounter module | Given locationId, returns Location |

## Regression Test Strategy

- Maintain existing test suite throughout all waves
- Each wave must pass all existing tests (possibly with updated imports)
- Add new contract tests for extracted module boundaries
- Use ArchUnit to enforce module dependency rules:
  - `patient` cannot import from `encounter` directly
  - All cross-module access through shared-kernel interfaces

## Related Documentation

- [Component Order](component-order.md)
- [Validation Criteria](validation-criteria.md)
- [Business Logic](../behavior/business-logic.md)
