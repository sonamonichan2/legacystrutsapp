> ⚠️ **Early Access**: Behavior documentation is in early access. Please review critically.

# Business Logic — OpenMRS Core 2.0.3

## Overview

Business logic resides primarily in the service implementation classes (`org.openmrs.api.impl.*`), with validation logic in validators (`org.openmrs.validator.*`) and cross-cutting concerns in AOP advice classes (`org.openmrs.aop.*`).

## PatientServiceImpl (1,590 lines)

### Patient Creation/Update
- `savePatient(Patient)`: Validates patient identifiers, triggers `RequiredDataAdvice` for audit fields, delegates to `PatientDAO.savePatient()`
- Auto-creates Person record if Patient is new (Patient extends Person)
- Validates at least one non-voided PatientIdentifier exists

### Patient Identifier Validation
- `checkPatientIdentifiers(Patient)`: Validates all identifiers
  - At least one required identifier type must be present
  - Check digit validation for identifier types that require it
  - Uniqueness check across the system
  - Format validation per PatientIdentifierType regex

### Patient Merging (Most Complex Business Logic)
- `mergePatients(Patient preferred, Patient notPreferred)`:
  - Moves all encounters from notPreferred to preferred
  - Moves all observations from notPreferred to preferred
  - Moves all orders from notPreferred to preferred
  - Moves all allergies from notPreferred to preferred
  - Moves patient identifiers, programs, relationships
  - Moves person names, addresses, attributes
  - Creates PersonMergeLog for audit trail
  - Voids the notPreferred patient
  - Handles duplicate detection and resolution
  - Serializes merge data for potential undo

### Allergy Management
- `setAllergies(Patient, Allergies)`: Replace all allergies for a patient
- Handles "No Known Allergies" status concept
- Validates allergens and reactions against concept dictionary

## PersonServiceImpl (939 lines)

### Person Management
- `savePerson(Person)`: Creates/updates person with names, addresses, attributes
- `voidPerson(Person, String)`: Soft-deletes with reason tracking
- `getSimilarPeople(String, Integer, String)`: Fuzzy matching for duplicate detection

### Attribute Management
- `savePersonAttributeType(PersonAttributeType)`: Manages extensible attribute types
- Validation: attribute types must have unique names, valid format classes
- Lock mechanism: `PersonAttributeTypeLockedException` when types are locked via global property

### Relationship Management
- `saveRelationship(Relationship)`: Creates person-to-person relationships
- `getRelationships(Person, Person, RelationshipType)`: Query relationships
- RelationshipType defines A-is-to-B and B-is-to-A labels

## EncounterServiceImpl (912 lines)

### Encounter Creation
- `saveEncounter(Encounter)`: Creates clinical encounter
  - Associates encounter with patient, location, provider(s)
  - Cascades save to contained observations and orders
  - Validates encounter type is set
  - Assigns encounter datetime if not set (defaults to now)

### Encounter Provider Assignment
- `EncounterProvider` maps providers to encounters with specific roles (`EncounterRole`)
- Multiple providers can participate in one encounter

### Encounter Transfer
- `transferEncounter(Encounter, Patient)`: Moves encounter to different patient
  - Updates all child observations and orders

## ObsServiceImpl (597 lines)

### Observation Recording
- `saveObs(Obs, String changeMessage)`: Creates/updates observations
  - On update: voids old obs, creates new obs with updated values (immutable pattern)
  - Validates concept datatype matches value type
  - Supports obs groups (parent-child obs hierarchy)

### Complex Observations
- `getComplexObs(Integer, String)`: Retrieves complex obs (files, images)
- Uses `ComplexObsHandler` strategy for different media types
- Complex data stored on filesystem, referenced by `valueComplex` field

## OrderServiceImpl (1,013 lines)

### Order Lifecycle
- **NEW**: `saveOrder(Order, OrderContext)` — Creates new order
  - Validates order type, concept, patient, encounter
  - Generates order number via `OrderNumberGenerator`
  - Sets dateActivated if not provided
  - Calculates autoExpireDate for DrugOrders based on duration/frequency

- **REVISE**: Revising an active order
  - Creates new order with `action = REVISE`
  - Links to previous order via `previousOrder`
  - Sets dateStopped on previous order

- **DISCONTINUE**: `discontinueOrder(Order, ...)`
  - Creates discontinuation order with `action = DISCONTINUE`
  - Links to discontinued order
  - Validates order is currently active

- **RENEW**: Renewing an expired/completed order

### Drug Order Specifics
- `DrugOrder` adds: dose, doseUnits, frequency, quantity, route, duration, numRefills
- `DosingInstructions` strategy pattern: `SimpleDosingInstructions`, `FreeTextDosingInstructions`
- Auto-expiry calculation based on duration and frequency

### Order Validation
- `OrderValidator`: Base order validation (patient, concept, orderer required)
- `DrugOrderValidator`: Drug-specific validation (dose, units, route)
- `TestOrderValidator`: Test-specific validation

## ConceptServiceImpl (1,891 lines — Largest Service)

### Concept Dictionary Management
- `saveConcept(Concept)`: Manages the medical concept dictionary
  - Validates concept names (no duplicates within locale)
  - Handles concept name tags (preferred, fully specified, short)
  - Manages concept mappings to external terminologies
  - Handles concept sets and answers

### Drug Management
- `saveDrug(Drug)`: Manages drug definitions
  - Links drugs to concepts (dosageForm, route)
  - Drug ingredient management

### Concept Proposals
- `saveConceptProposal(ConceptProposal)`: Workflow for proposed new concepts
  - Status lifecycle: UNMAPPED → mapped/rejected

## UserServiceImpl (664 lines)

### Authentication & Authorization
- Password management with configurable policies:
  - Minimum length enforcement
  - Character complexity requirements (uppercase, lowercase, digits)
  - Password expiration via global properties
- Password hashing via `Security.hashSecret()`
- User system ID auto-generation

### Role/Privilege Management
- Hierarchical roles (roles can inherit from parent roles)
- Privileges are granular permissions checked by `AuthorizationAdvice`

## ProgramWorkflowServiceImpl (534 lines)

### Program Enrollment
- `savePatientProgram(PatientProgram)`: Enrolls patient in clinical program
  - Sets enrollment date, completion date
  - Associates with location

### State Transitions
- `PatientState` tracks current state in a `ProgramWorkflow`
  - States are ordered within a workflow
  - State change rules enforce valid transitions
  - `ConceptStateConversion` allows automatic state transitions based on program/workflow/concept

## AOP Cross-Cutting Concerns

### AuthorizationAdvice
- Intercepts all service method calls
- Reads `@Authorized` annotation to determine required privileges
- Checks current user's roles/privileges via `UserContext`
- Throws `APIAuthenticationException` if unauthorized

### RequiredDataAdvice
- Auto-populates audit fields on save operations:
  - `creator`, `dateCreated` (new objects)
  - `changedBy`, `dateChanged` (existing objects)
  - `voidedBy`, `dateVoided`, `voidReason` (void operations)
  - `retiredBy`, `dateRetired`, `retireReason` (retire operations)
- Recursively processes child objects (collections)

### LoggingAdvice
- Logs method entry with parameters
- Logs method exit with return value
- Logs execution time for performance monitoring

## SchedulerService

### Task Scheduling
- `SchedulerService` manages recurring tasks via `java.util.Timer`
- `TaskDefinition` stores task configuration in database
- Built-in tasks:
  - `ProcessHL7InQueueTask` — processes HL7 messages from queue
  - `AutoCloseVisitsTask` — auto-closes open visits
  - `AlertReminderTask` — sends alert reminders
  - `CheckInternetConnectivityTask` — connectivity check
  - `SendEmailTask` — email notification sending

## Related Documentation

- [Workflows](workflows.md)
- [Decision Logic](decision-logic.md)
- [Error Handling](error-handling.md)
- [Components](../architecture/components.md)
- [Interfaces](../reference/interfaces.md)
