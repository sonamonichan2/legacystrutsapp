# OpenMRS Core - Proposed Modular-Monolith Target Structure

## Overview

This document defines the target modular-monolith package structure for OpenMRS Core,
mapping the existing 649+ Java source files to 12 logical modules within the same
deployable WAR artifact.

## Module Dependency Graph

```
                    ┌──────────────┐
                    │ shared-kernel │
                    └──────┬───────┘
           ┌───────────────┼───────────────┐
           │               │               │
    ┌──────▼──────┐ ┌──────▼──────┐ ┌──────▼──────┐
    │   location  │ │   person    │ │  concepts   │
    └──────┬──────┘ └──────┬──────┘ └──────┬──────┘
           │        ┌──────┼───────────────┤
           │        │      │               │
    ┌──────▼──────▼─▼┐     │        ┌──────▼──────┐
    │    patient     │     │        │ users-admin │
    └──────┬─────────┘     │        └─────────────┘
           │               │
    ┌──────▼───────────────▼──┐
    │      encounter          │
    └──────┬──────────────────┘
           │
    ┌──────▼──────┐  ┌───────────┐
    │ observation │  │  orders   │
    └─────────────┘  └───────────┘
           │               │
    ┌──────▼───────────────▼──┐
    │    integration (HL7)    │
    └─────────────────────────┘
           │
    ┌──────▼──────────────────┐
    │   search-reporting      │
    └─────────────────────────┘
           │
    ┌──────▼──────────────────┐
    │     web-adapter         │
    └─────────────────────────┘
```

## Module Definitions

---

### 1. shared-kernel

**Package**: `org.openmrs.module.sharedkernel`

**Responsibility**: Minimal shared primitives and contracts that all modules depend on.
Zero business logic - only interfaces, base classes, and utilities.

#### Package Structure:
```
org.openmrs.module.sharedkernel
├── domain/
│   ├── OpenmrsObjectContract.java      ← from OpenmrsObject
│   ├── OpenmrsDataContract.java        ← from OpenmrsData
│   ├── OpenmrsMetadataContract.java    ← from OpenmrsMetadata
│   ├── AuditableContract.java          ← from Auditable
│   ├── VoidableContract.java           ← from Voidable
│   └── RetireableContract.java         ← from Retireable
├── api/
│   ├── ServiceContract.java            ← from OpenmrsService
│   └── APIException (reference)
├── db/
│   ├── DbSession (reference)
│   ├── DbSessionFactory (reference)
│   └── Generic DAO interfaces
├── util/
│   ├── OpenmrsUtil (reference)
│   ├── OpenmrsConstants (reference)
│   └── PrivilegeConstants (reference)
└── annotation/
    ├── Authorized (reference)
    └── AllowDirectAccess (reference)
```

**Current Source Classes** (to be shared):
- `org.openmrs.BaseOpenmrsObject` (abstract base class)
- `org.openmrs.BaseOpenmrsData` (abstract base class)
- `org.openmrs.BaseOpenmrsMetadata` (abstract base class)
- `org.openmrs.OpenmrsObject` (interface)
- `org.openmrs.OpenmrsData` (interface)
- `org.openmrs.OpenmrsMetadata` (interface)
- `org.openmrs.Auditable` (interface)
- `org.openmrs.Voidable` (interface)
- `org.openmrs.Retireable` (interface)
- `org.openmrs.Attributable` (interface)
- `org.openmrs.api.OpenmrsService` (interface)
- `org.openmrs.api.APIException`
- `org.openmrs.api.APIAuthenticationException`
- `org.openmrs.annotation.*` (authorization annotations)
- `org.openmrs.util.OpenmrsUtil`
- `org.openmrs.util.OpenmrsConstants`
- `org.openmrs.util.PrivilegeConstants`
- `org.openmrs.customdatatype.*` (custom datatype framework)
- `org.openmrs.attribute.*` (attribute framework)
- `org.openmrs.BaseCustomizableData`, `org.openmrs.BaseCustomizableMetadata`

**Dependencies**: None (leaf module)

---

### 2. person

**Package**: `org.openmrs.module.person`

**Responsibility**: Person demographics, names, addresses, attributes, and relationships.

#### Package Structure:
```
org.openmrs.module.person
├── domain/
│   ├── PersonContract.java
│   ├── PersonNameContract.java
│   ├── PersonAddressContract.java
│   ├── PersonAttributeContract.java
│   └── RelationshipContract.java
├── api/
│   ├── PersonModuleService.java
│   └── impl/
│       └── PersonModuleServiceImpl.java
├── db/
│   ├── PersonDAO.java
│   └── hibernate/
│       └── HibernatePersonDAO.java
└── validator/
    ├── PersonValidator.java
    ├── PersonNameValidator.java
    └── PersonAddressValidator.java
```

**Current Source Classes**:
- `org.openmrs.Person` + PersonName, PersonAddress, PersonAttribute, PersonAttributeType
- `org.openmrs.Relationship`, `org.openmrs.RelationshipType`
- `org.openmrs.api.PersonService` / `PersonServiceImpl`
- `org.openmrs.api.db.PersonDAO` / `HibernatePersonDAO`
- `org.openmrs.validator.PersonValidator`, PersonNameValidator, PersonAddressValidator
- `org.openmrs.person.*` (PersonMergeLog, etc.)
- `org.openmrs.propertyeditor.PersonEditor`, PersonAttributeTypeEditor

**Dependencies**: shared-kernel

---

### 3. patient (depends on person, concepts, location)

**Package**: `org.openmrs.module.patient`

**Responsibility**: Patient records, identifiers, allergies, and patient-specific operations.

#### Package Structure:
```
org.openmrs.module.patient
├── domain/
│   ├── PatientContract.java
│   ├── PatientIdentifierContract.java
│   └── AllergyContract.java
├── api/
│   ├── PatientModuleService.java
│   └── impl/
│       └── PatientModuleServiceImpl.java
├── db/
│   ├── PatientDAO.java
│   └── hibernate/
│       └── HibernatePatientDAO.java
└── validator/
    ├── PatientValidator.java
    └── PatientIdentifierValidator.java
```

**Current Source Classes**:
- `org.openmrs.Patient` (extends Person - key coupling point)
- `org.openmrs.PatientIdentifier`, `org.openmrs.PatientIdentifierType`
- `org.openmrs.Allergy*` (Allergen, Allergies, Allergy, AllergyReaction, etc.)
- `org.openmrs.api.PatientService` / `PatientServiceImpl`
- `org.openmrs.api.db.PatientDAO` / `HibernatePatientDAO`
- `org.openmrs.validator.PatientValidator`, PatientIdentifierValidator
- `org.openmrs.patient.*` (IdentifierValidator implementations)

**Dependencies**: shared-kernel, person, concepts, location

---

### 4. concepts

**Package**: `org.openmrs.module.concepts`

**Responsibility**: Clinical concepts, drugs, concept mappings, and terminology management.

#### Package Structure:
```
org.openmrs.module.concepts
├── domain/
│   ├── ConceptContract.java
│   ├── ConceptClassContract.java
│   ├── ConceptDatatypeContract.java
│   ├── DrugContract.java
│   └── ... (20+ concept types)
├── api/
│   ├── ConceptModuleService.java
│   └── impl/
│       └── ConceptModuleServiceImpl.java
├── db/
│   ├── ConceptDAO.java
│   └── hibernate/
│       └── HibernateConceptDAO.java
└── validator/
    └── ConceptValidator.java
```

**Current Source Classes** (20+ types):
- `org.openmrs.Concept`, ConceptAnswer, ConceptAttribute, ConceptAttributeType
- ConceptClass, ConceptComplex, ConceptDatatype, ConceptDescription
- ConceptMap, ConceptMapType, ConceptName, ConceptNameTag
- ConceptNumeric, ConceptProposal, ConceptReferenceTerm, ConceptReferenceTermMap
- ConceptSearchResult, ConceptSet, ConceptSource, ConceptStateConversion, ConceptStopWord
- `org.openmrs.Drug`, DrugIngredient, DrugReferenceMap, BaseConceptMap
- `org.openmrs.api.ConceptService` / `ConceptServiceImpl`
- `org.openmrs.api.db.ConceptDAO` / `HibernateConceptDAO`

**Dependencies**: shared-kernel

---

### 5. encounter (depends on patient, concepts, location)

**Package**: `org.openmrs.module.encounter`

**Responsibility**: Clinical encounters, encounter types, and encounter-provider relationships.

#### Package Structure:
```
org.openmrs.module.encounter
├── domain/
│   ├── EncounterContract.java
│   ├── EncounterTypeContract.java
│   └── EncounterRoleContract.java
├── api/
│   ├── EncounterModuleService.java
│   └── impl/
├── db/
│   └── hibernate/
└── validator/
    └── EncounterValidator.java
```

**Current Source Classes**:
- `org.openmrs.Encounter`, EncounterProvider, EncounterRole, EncounterType
- `org.openmrs.Form`, FormField, FormResource, Field, FieldAnswer, FieldType
- `org.openmrs.api.EncounterService` / `EncounterServiceImpl`
- `org.openmrs.api.FormService` / `FormServiceImpl`

**Dependencies**: shared-kernel, patient, concepts, location

---

### 6. observation (depends on encounter, concepts, person)

**Package**: `org.openmrs.module.observation`

**Responsibility**: Clinical observations, complex observation handling.

#### Package Structure:
```
org.openmrs.module.observation
├── domain/
│   └── ObsContract.java
├── api/
│   ├── ObservationModuleService.java
│   └── impl/
├── db/
│   └── hibernate/
├── handler/
│   └── ComplexObsHandler implementations
└── validator/
    └── ObsValidator.java
```

**Current Source Classes**:
- `org.openmrs.Obs`, `org.openmrs.obs.*` (ComplexData, ComplexObsHandler, etc.)
- `org.openmrs.api.ObsService` / `ObsServiceImpl`
- `org.openmrs.api.handler.ObsHandler` implementations

**Dependencies**: shared-kernel, encounter, concepts, person

---

### 7. orders (depends on patient, encounter, concepts)

**Package**: `org.openmrs.module.orders`

**Responsibility**: Clinical orders, drug orders, test orders, order management.

#### Package Structure:
```
org.openmrs.module.orders
├── domain/
│   ├── OrderContract.java
│   ├── DrugOrderContract.java
│   └── TestOrderContract.java
├── api/
│   ├── OrderModuleService.java
│   ├── OrderSetModuleService.java
│   └── impl/
├── db/
│   └── hibernate/
└── validator/
    ├── OrderValidator.java
    ├── DrugOrderValidator.java
    └── TestOrderValidator.java
```

**Current Source Classes**:
- `org.openmrs.Order`, DrugOrder, TestOrder, OrderType, OrderFrequency
- OrderGroup, OrderSet, OrderSetMember, CareSetting
- DosingInstructions, SimpleDosingInstructions, FreeTextDosingInstructions, Duration
- `org.openmrs.api.OrderService`, OrderSetService / implementations
- `org.openmrs.order.*` (OrderUtil, etc.)

**Dependencies**: shared-kernel, patient, encounter, concepts

---

### 8. users-admin (depends on person)

**Package**: `org.openmrs.module.usersadmin`

**Responsibility**: User management, roles, privileges, authentication, system administration.

#### Package Structure:
```
org.openmrs.module.usersadmin
├── domain/
│   ├── UserContract.java
│   ├── RoleContract.java
│   └── PrivilegeContract.java
├── api/
│   ├── UserAdminModuleService.java
│   └── impl/
├── db/
│   └── hibernate/
└── validator/
    └── UserValidator.java
```

**Current Source Classes**:
- `org.openmrs.User`, Role, Privilege
- `org.openmrs.api.db.LoginCredential`
- `org.openmrs.api.UserService` / `UserServiceImpl`
- `org.openmrs.api.AdministrationService` / `AdministrationServiceImpl`
- `org.openmrs.GlobalProperty`, ImplementationId

**Dependencies**: shared-kernel, person

---

### 9. location

**Package**: `org.openmrs.module.location`

**Responsibility**: Care locations, location hierarchy, location tags and attributes.

#### Package Structure:
```
org.openmrs.module.location
├── domain/
│   ├── LocationContract.java
│   ├── LocationTagContract.java
│   ├── LocationAttributeContract.java
│   └── LocationAttributeTypeContract.java
├── api/
│   ├── LocationModuleService.java
│   └── impl/
│       └── LocationModuleServiceImpl.java
├── db/
│   ├── LocationDAO.java
│   └── hibernate/
│       └── HibernateLocationDAO.java
└── validator/
    └── LocationValidator.java
```

**Current Source Classes**:
- `org.openmrs.Location`, LocationTag, LocationAttribute, LocationAttributeType
- `org.openmrs.Address` (shared interface)
- `org.openmrs.api.LocationService` / `LocationServiceImpl`
- `org.openmrs.api.db.LocationDAO` / `HibernateLocationDAO`

**Dependencies**: shared-kernel (least coupled module)

---

### 10. integration (HL7, Notification)

**Package**: `org.openmrs.module.integration`

**Responsibility**: External system integration, HL7 message processing, system notifications.

#### Package Structure:
```
org.openmrs.module.integration
├── hl7/
│   ├── HL7ModuleService.java
│   ├── HL7MessageContract.java
│   ├── handler/
│   │   ├── ADTA28Handler.java
│   │   └── ORUR01Handler.java
│   ├── impl/
│   └── db/
├── notification/
│   ├── NotificationModuleService.java
│   ├── Alert, AlertRecipient, Template
│   ├── impl/
│   └── db/
└── api/
    └── IntegrationFacadeService.java
```

**Current Source Classes**:
- `org.openmrs.hl7.*` (16 classes): HL7Service, HL7ServiceImpl, HL7DAO, etc.
- `org.openmrs.hl7.handler.*`: ADTA28Handler, ORUR01Handler
- `org.openmrs.notification.*` (20 classes): AlertService, MessageService, Template, etc.

**Dependencies**: shared-kernel, patient, person, concepts, encounter, observation, location
(HL7 handlers have wide cross-domain dependencies)

---

### 11. search-reporting

**Package**: `org.openmrs.module.searchreporting`

**Responsibility**: Search infrastructure, cohort management, reporting utilities.

#### Package Structure:
```
org.openmrs.module.searchreporting
├── api/
│   ├── SearchReportingModuleService.java
│   └── impl/
├── cohort/
│   ├── CohortService (reference)
│   └── CohortServiceImpl (reference)
├── search/
│   └── Lucene/Hibernate Search integration
└── db/
    └── hibernate/
```

**Current Source Classes**:
- `org.openmrs.Cohort`
- `org.openmrs.api.CohortService` / `CohortServiceImpl`
- Hibernate Search / Lucene integration classes
- `org.openmrs.api.db.CohortDAO` / `HibernateCohortDAO`

**Dependencies**: shared-kernel, patient

---

### 12. web-adapter

**Package**: `org.openmrs.module.webadapter` (in web module)

**Responsibility**: HTTP layer, filters, servlets, controllers, REST adapters.

#### Package Structure:
```
org.openmrs.module.webadapter
├── api/
│   └── WebModuleAdapter.java
├── filter/
│   ├── InitializationFilter
│   ├── UpdateFilter
│   ├── StartupErrorFilter
│   ├── GZIPFilter
│   └── OpenmrsFilter
├── controller/
│   └── PseudoStaticContentController
├── servlet/
│   ├── DispatcherServlet
│   ├── StaticDispatcherServlet
│   ├── ModuleResourcesServlet
│   └── ModuleServlet
└── support/
    ├── Listener
    ├── WebConstants
    ├── WebUtil
    └── OpenmrsBindingInitializer
```

**Current Source Classes** (39 files in web module):
- All filter classes in `org.openmrs.web.filter.*`
- Servlet classes in `org.openmrs.web.*`
- Module web integration in `org.openmrs.module.web.*`
- Controller in `org.openmrs.web.controller.*`

**Dependencies**: All api modules (accesses via Context.getService() pattern)

---

## Class Migration Summary

| Module | Estimated Classes | Key Entities |
|--------|------------------|-------------|
| shared-kernel | ~40 | Base classes, interfaces, utilities, annotations |
| person | ~25 | Person, PersonName, PersonAddress, Relationship |
| patient | ~20 | Patient, PatientIdentifier, Allergy |
| concepts | ~35 | Concept (20+ types), Drug |
| encounter | ~15 | Encounter, EncounterType, Form |
| observation | ~10 | Obs, ComplexData |
| orders | ~20 | Order, DrugOrder, TestOrder, OrderSet |
| users-admin | ~15 | User, Role, Privilege, GlobalProperty |
| location | ~8 | Location, LocationTag, LocationAttribute |
| integration | ~36 | HL7 (16) + Notification (20) |
| search-reporting | ~10 | Cohort, Search integration |
| web-adapter | ~39 | Filters, Servlets, Controllers |
| **Total** | **~273** | Core classes (remaining are support) |

## Migration Sequence

1. **Wave 1** (Current): Establish package structure and interface contracts
2. **Wave 2**: Move Location module (least coupled)
3. **Wave 3**: Move Person module (foundational)
4. **Wave 4**: Move Concepts module (self-contained)
5. **Wave 5**: Move Patient, Encounter, Observation, Orders (cross-domain)
6. **Wave 6**: Move Users-Admin and Integration
7. **Wave 7**: Split Spring configuration per module
8. **Wave 8**: Split Hibernate configuration per module
9. **Wave 9**: Extract web-adapter, refactor Context service locator
