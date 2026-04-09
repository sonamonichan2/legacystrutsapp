# Program Structure — OpenMRS Core 2.0.3

## Package Hierarchy

```
org.openmrs/                          # Root package — Domain entities (~105 files)
├── annotation/                       # Custom annotations (@Authorized, @Handler, etc.)
├── aop/                              # AOP advice (AuthorizationAdvice, LoggingAdvice, RequiredDataAdvice)
├── api/                              # Service layer
│   ├── (root)                        # Service interfaces + custom exceptions (~52 files)
│   ├── impl/                         # Service implementations (~20 files)
│   ├── context/                      # Context, ServiceContext, UserContext, Daemon
│   ├── db/                           # DAO interfaces (~27 files)
│   │   └── hibernate/                # Hibernate DAO implementations + interceptors (~44 files)
│   │       └── search/               # Hibernate Search integration (LuceneQuery, CriteriaQuery)
│   │           └── bridge/           # Lucene field bridges
│   └── handler/                      # Handler utilities
├── attribute/                        # Attribute type framework
├── collection/                       # Custom collections
├── comparator/                       # Comparators for domain entities
├── customdatatype/                   # Custom datatype framework
│   ├── datatype/                     # Built-in datatypes
│   └── handler/                      # Datatype handlers
├── hl7/                              # HL7 processing
│   ├── handler/                      # HL7 message handlers (ADT_A28, ORU_R01)
│   ├── db/                           # HL7 DAO interfaces
│   │   └── hibernate/                # HL7 Hibernate DAOs
│   └── impl/                         # HL7 service implementation
├── layout/                           # Name/address layout formatters
├── logic/                            # Logic rule engine (deprecated)
├── messagesource/                    # I18n message sources
├── migration/                        # Database migration utilities
├── module/                           # Module/plugin system (~22 files)
│   └── web/                          # Module web integration (in web module)
│       └── filter/                   # Module filter chain
├── notification/                     # Alert/notification system
│   ├── db/                           # Notification DAOs
│   │   └── hibernate/                # Hibernate notification DAOs
│   └── impl/                         # Notification service impl
├── obs/                              # Complex observation handlers
├── order/                            # Order utilities
├── parameter/                        # Search parameter objects
├── patient/                          # Patient identifier types/validators
├── person/                           # Person merge utilities
├── propertyeditor/                   # Spring property editors
├── scheduler/                        # Task scheduling
│   ├── db/                           # Scheduler DAOs
│   │   └── hibernate/                # Hibernate scheduler DAOs
│   ├── tasks/                        # Built-in scheduled tasks
│   └── timer/                        # Timer-based scheduling
├── serialization/                    # Serialization framework
├── util/                             # Utilities (~30+ files)
└── validator/                        # Spring validators (~56 files)

org.openmrs.web/                      # Web module root (in web module)
├── (root)                            # Listener, DispatcherServlet, StaticDispatcherServlet
├── controller/                       # PseudoStaticContentController
└── filter/                           # Servlet filters
    ├── initialization/               # Setup wizard
    ├── update/                       # Database update
    ├── startuperror/                 # Error display
    └── util/                         # Filter utilities
```

## Entity Class Hierarchy

### Base Classes
```
OpenmrsObject (interface)
└── BaseOpenmrsObject (abstract) — uuid, getId(), equals()/hashCode()
    ├── OpenmrsData (interface extends OpenmrsObject, Auditable, Voidable)
    │   └── BaseOpenmrsData (abstract) — creator, dateCreated, changedBy, dateChanged, voided, voidedBy, dateVoided, voidReason
    │       ├── BaseCustomizableData<A> — attributes collection
    │       └── (Direct subclasses: ConceptAnswer, ConceptMap, ConceptName, ...)
    └── OpenmrsMetadata (interface extends OpenmrsObject, Auditable, Retireable)
        └── BaseOpenmrsMetadata (abstract) — name, description, creator, dateCreated, retired, retiredBy, dateRetired, retireReason
            ├── BaseCustomizableMetadata<A> — attributes collection
            └── (Direct subclasses: ConceptClass, ConceptDatatype, EncounterType, ...)
```

### Core Domain Entities (by subdomain)

**Person Domain**:
- `Person` extends `BaseOpenmrsData` — core demographics (gender, birthdate, dead, deathDate)
- `PersonName` extends `BaseOpenmrsData` — person names (givenName, middleName, familyName)
- `PersonAddress` extends `BaseOpenmrsData` — person addresses
- `PersonAttribute` extends `BaseOpenmrsData` — extensible attributes
- `PersonAttributeType` extends `BaseOpenmrsMetadata` — attribute type definitions
- `Relationship` extends `BaseOpenmrsData` — relationships between persons
- `RelationshipType` extends `BaseOpenmrsMetadata` — relationship type definitions

**Patient Domain** (extends Person):
- `Patient` extends `Person` — patient-specific data (identifiers)
- `PatientIdentifier` extends `BaseOpenmrsData` — patient identifiers
- `PatientIdentifierType` extends `BaseOpenmrsMetadata` — identifier type definitions
- `Allergy` extends `BaseOpenmrsData` — patient allergies
- `AllergyReaction` extends `BaseOpenmrsData` — allergy reactions

**Encounter Domain**:
- `Encounter` extends `BaseOpenmrsData` — clinical encounters (patient, location, provider, obs)
- `EncounterType` extends `BaseOpenmrsMetadata` — encounter type definitions
- `EncounterProvider` extends `BaseOpenmrsData` — encounter-provider mapping
- `EncounterRole` extends `BaseOpenmrsMetadata` — encounter role definitions

**Observation Domain**:
- `Obs` extends `BaseOpenmrsData` — clinical observations (concept, value, encounter)
- `ConceptComplex` extends `Concept` — complex observations (files, images)

**Order Domain**:
- `Order` extends `BaseOpenmrsData` — base order (patient, concept, orderer, urgency, action)
- `DrugOrder` extends `Order` — drug orders (dose, frequency, duration, quantity, route)
- `TestOrder` extends `Order` — test orders (laterality, clinicalHistory)
- `OrderType` extends `BaseOpenmrsMetadata` — order type definitions
- `OrderFrequency` extends `BaseOpenmrsMetadata` — dosing frequencies
- `OrderGroup` extends `BaseOpenmrsData` — order grouping
- `OrderSet` extends `BaseOpenmrsMetadata` — predefined order sets
- `OrderSetMember` extends `BaseOpenmrsData` — order set members
- `CareSetting` extends `BaseOpenmrsMetadata` — inpatient/outpatient settings

**Concept Domain**:
- `Concept` extends `BaseOpenmrsData` — medical concepts
- `ConceptNumeric` extends `Concept` — numeric concepts with ranges
- `ConceptComplex` extends `Concept` — complex data concepts
- `ConceptName` extends `BaseOpenmrsData` — concept names
- `ConceptDescription` extends `BaseOpenmrsData` — concept descriptions
- `ConceptAnswer` extends `BaseOpenmrsData` — coded answers
- `ConceptSet` extends `BaseOpenmrsData` — concept grouping
- `ConceptMap` extends `BaseConceptMap` — concept mappings
- `ConceptSource` extends `BaseOpenmrsMetadata` — terminology sources
- `ConceptClass` extends `BaseOpenmrsMetadata` — concept classification
- `ConceptDatatype` extends `BaseOpenmrsMetadata` — concept data types
- `Drug` extends `BaseOpenmrsMetadata` — drug definitions
- plus many more concept-related classes

**Location Domain**:
- `Location` extends `BaseCustomizableMetadata` — healthcare facility locations
- `LocationTag` extends `BaseOpenmrsMetadata` — location tags
- `LocationAttribute` extends `BaseAttribute` — location attributes
- `LocationAttributeType` extends `BaseAttributeType` — location attribute types

**Visit Domain**:
- `Visit` extends `BaseCustomizableData` — patient visits
- `VisitType` extends `BaseOpenmrsMetadata` — visit types
- `VisitAttribute` extends `BaseAttribute` — visit attributes
- `VisitAttributeType` extends `BaseAttributeType` — visit attribute types

**Provider Domain**:
- `Provider` extends `BaseCustomizableMetadata` — healthcare providers
- `ProviderAttribute` extends `BaseAttribute`
- `ProviderAttributeType` extends `BaseAttributeType`

**User/Security Domain**:
- `User` extends `BaseOpenmrsMetadata` — user accounts
- `Role` extends `BaseOpenmrsMetadata` — security roles
- `Privilege` extends `BaseOpenmrsMetadata` — granular permissions

**Program Domain**:
- `Program` extends `BaseOpenmrsMetadata` — clinical programs
- `ProgramWorkflow` extends `BaseOpenmrsMetadata` — workflows within programs
- `ProgramWorkflowState` extends `BaseOpenmrsMetadata` — states within workflows
- `PatientProgram` extends `BaseOpenmrsData` — patient enrollment
- `PatientState` extends `BaseOpenmrsData` — patient's current state

**Form Domain**:
- `Form` extends `BaseOpenmrsMetadata` — clinical forms
- `FormField` extends `BaseOpenmrsMetadata` — form fields
- `Field` extends `BaseOpenmrsMetadata` — field definitions
- `FieldType` extends `BaseOpenmrsMetadata` — field types
- `FormResource` extends `BaseOpenmrsData` — form resources

**Other**:
- `Cohort` extends `BaseOpenmrsData` — patient cohorts
- `GlobalProperty` extends `BaseOpenmrsObject` — system configuration
- `ImplementationId` — deployment identifier

## Related Documentation

- [Components](../architecture/components.md)
- [Interfaces](interfaces.md)
- [Data Models](data-models.md)
- [API Reference](api-reference.md)
