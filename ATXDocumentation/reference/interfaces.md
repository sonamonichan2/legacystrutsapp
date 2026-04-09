# Service Interfaces — OpenMRS Core 2.0.3

## Overview

All service interfaces extend `OpenmrsService` and are accessed through the `Context` service locator. Services are proxied by Spring AOP for authorization, logging, and audit field management.

## Core Service Interfaces

### PatientService (`org.openmrs.api.PatientService`)
- **Lines**: 843 | **Public Methods**: ~51
- **Purpose**: Patient CRUD, identifier management, patient merging
- **Key Methods**:
  - `savePatient(Patient)` — Create/update patient
  - `getPatient(Integer)`, `getPatientByUuid(String)` — Retrieve patient
  - `voidPatient(Patient, String)` / `unvoidPatient(Patient)` — Soft delete
  - `mergePatients(Patient preferred, Patient notPreferred)` — Merge patient records
  - `getPatients(String query, Integer start, Integer length)` — Search patients
  - `checkPatientIdentifiers(Patient)` — Validate identifiers
  - `savePatientIdentifierType(PatientIdentifierType)` — Manage identifier types
  - `getAllPatients()`, `getAllPatients(boolean includeVoided)` — List all patients
  - `getAllergies(Patient)`, `setAllergies(Patient, Allergies)` — Allergy management

### PersonService (`org.openmrs.api.PersonService`)
- **Lines**: 844 | **Public Methods**: ~65
- **Purpose**: Person demographics, attributes, names, addresses, relationships
- **Key Methods**:
  - `savePerson(Person)`, `getPerson(Integer)`, `getPersonByUuid(String)`
  - `voidPerson(Person, String)` / `unvoidPerson(Person)`
  - `savePersonName(PersonName)`, `savePersonAddress(PersonAddress)`
  - `savePersonAttributeType(PersonAttributeType)`, `getPersonAttributeType(Integer)`
  - `getRelationships(...)`, `saveRelationship(Relationship)`, `getRelationshipTypes(...)`
  - `getSimilarPeople(String, Integer, String)` — Find similar persons

### EncounterService (`org.openmrs.api.EncounterService`)
- **Lines**: 727 | **Public Methods**: ~51
- **Purpose**: Clinical encounter management, encounter types, encounter roles
- **Key Methods**:
  - `saveEncounter(Encounter)`, `getEncounter(Integer)`, `getEncounterByUuid(String)`
  - `voidEncounter(Encounter, String)` / `unvoidEncounter(Encounter)`
  - `getEncounters(Patient, Location, Date, Date, ...)` — Search encounters
  - `getEncountersByVisit(Visit, boolean)` — Get encounters by visit
  - `saveEncounterType(EncounterType)`, `saveEncounterRole(EncounterRole)`
  - `transferEncounter(Encounter, Patient)` — Transfer encounter to another patient

### ObsService (`org.openmrs.api.ObsService`)
- **Lines**: 481 | **Public Methods**: ~25
- **Purpose**: Clinical observation CRUD, complex obs handling
- **Key Methods**:
  - `saveObs(Obs, String)`, `getObs(Integer)`, `getObsByUuid(String)`
  - `voidObs(Obs, String)` / `unvoidObs(Obs)`
  - `getObservations(List<Person>, List<Encounter>, List<Concept>, ...)` — Search observations
  - `getObservationsByPersonAndConcept(Person, Concept)` — Filter by person+concept
  - `getComplexObs(Integer, String)` — Retrieve complex observations

### OrderService (`org.openmrs.api.OrderService`)
- **Lines**: 788 | **Public Methods**: ~55
- **Purpose**: Order lifecycle — creation, activation, revision, discontinuation
- **Key Methods**:
  - `saveOrder(Order, OrderContext)` — Create/save order with context
  - `getOrder(Integer)`, `getOrderByUuid(String)`, `getOrderByOrderNumber(String)`
  - `voidOrder(Order, String)` / `unvoidOrder(Order)`
  - `discontinueOrder(Order, Concept, Date, Provider, Encounter)` — Discontinue active order
  - `reviseOrder(Order)` — Revise existing order
  - `getActiveOrders(Patient, OrderType, CareSetting, Date)` — Get active orders
  - `getOrderType(Integer)`, `saveOrderType(OrderType)` — Order type management
  - `getOrderFrequencies(...)`, `saveOrderFrequency(OrderFrequency)` — Frequency management
  - `getCareSetting(Integer)`, `getCareSettings(boolean)` — Care setting management

### ConceptService (`org.openmrs.api.ConceptService`)
- **Lines**: 1865 | **Public Methods**: ~140
- **Purpose**: Medical concept dictionary management (largest service interface)
- **Key Methods**:
  - `saveConcept(Concept)`, `getConcept(Integer)`, `getConceptByUuid(String)`
  - `getConceptByName(String)`, `getConceptByMapping(String, String)`
  - `getConceptsByName(String)` — Search concepts by name
  - `getAllConcepts()`, `getAllConcepts(String, boolean, boolean)` — List concepts
  - `saveDrug(Drug)`, `getDrug(Integer)`, `getDrugs(String)` — Drug management
  - `saveConceptSource(ConceptSource)` — Terminology source management
  - `getConceptReferenceTerm(Integer)`, `saveConceptReferenceTerm(...)` — Reference term management
  - `getConceptMapType(Integer)`, `saveConceptMapType(...)` — Map type management
  - `purgeConcept(Concept)` — Permanent deletion
  - `getConceptProposal(Integer)` — Concept proposals

### LocationService (`org.openmrs.api.LocationService`)
- **Lines**: 502 | **Public Methods**: ~32
- **Purpose**: Healthcare facility locations, tags, attributes
- **Key Methods**:
  - `saveLocation(Location)`, `getLocation(Integer)`, `getLocationByUuid(String)`
  - `getLocationByName(String)`, `getAllLocations(boolean)`
  - `retireLocation(Location, String)` / `unretireLocation(Location)`
  - `saveLocationTag(LocationTag)`, `getLocationTags(String)` — Tag management
  - `saveLocationAttributeType(...)` — Attribute type management

### UserService (`org.openmrs.api.UserService`)
- **Lines**: 555 | **Public Methods**: ~43
- **Purpose**: User account management, role/privilege administration
- **Key Methods**:
  - `saveUser(User, String)`, `getUser(Integer)`, `getUserByUuid(String)`
  - `getUserByUsername(String)`, `getUsers(String, List<Role>, boolean)`
  - `changePassword(User, String, String)` — Password management
  - `saveRole(Role)`, `savePrivilege(Privilege)` — Security management
  - `getAllRoles()`, `getAllPrivileges()` — List roles/privileges
  - `purgeUser(User)`, `retireUser(User, String)` — User lifecycle

### VisitService (`org.openmrs.api.VisitService`)
- **Lines**: 382 | **Public Methods**: ~15
- **Purpose**: Patient visit management
- **Key Methods**:
  - `saveVisit(Visit)`, `getVisit(Integer)`, `getVisitByUuid(String)`
  - `voidVisit(Visit, String)` / `unvoidVisit(Visit)`
  - `getVisitTypes()`, `saveVisitType(VisitType)` — Visit type management
  - `getActiveVisitsByPatient(Patient)` — Active visits

### ProviderService (`org.openmrs.api.ProviderService`)
- **Lines**: 324 | **Public Methods**: ~28
- **Purpose**: Healthcare provider management
- **Key Methods**:
  - `saveProvider(Provider)`, `getProvider(Integer)`, `getProviderByUuid(String)`
  - `getProviders(String, Integer, Integer, Map)` — Search providers
  - `retireProvider(Provider, String)` / `unretireProvider(Provider)` — Lifecycle
  - `saveProviderAttributeType(ProviderAttributeType)` — Attribute types

### Additional Service Interfaces

| Service | Lines | Methods | Purpose |
|---------|-------|---------|---------|
| `AdministrationService` | ~1000+ | ~80+ | Global properties, system settings, SQL execution |
| `FormService` | ~600+ | ~40+ | Clinical form management |
| `CohortService` | ~200+ | ~15+ | Patient cohort management |
| `ProgramWorkflowService` | ~500+ | ~35+ | Program enrollment, state transitions |
| `SerializationService` | ~100+ | ~5+ | Object serialization/deserialization |
| `DatatypeService` | ~100+ | ~10+ | Custom datatype management |
| `OrderSetService` | ~150+ | ~10+ | Order set management |

## Common Interface Patterns

All services follow these conventions:
1. **CRUD**: `save*()`, `get*()`, `getAll*()`, `void*()` / `retire*()`, `unvoid*()` / `unretire*()`, `purge*()`
2. **Authorization**: Methods annotated with `@Authorized({privilege1, privilege2})`
3. **Transaction**: Methods annotated with `@Transactional` (read-only where applicable)
4. **Handler Registration**: `@Handler(supports={Class})` annotation on implementations

## Related Documentation

- [Program Structure](program-structure.md)
- [Data Models](data-models.md)
- [Components](../architecture/components.md)
- [API Reference](api-reference.md)
