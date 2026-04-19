# OpenMRS Core - Dependency and Coupling Analysis

## 1. Entity Cross-References (Quantified)

References counted across all 649 Java source files in the api module:

| Entity | Reference Count | Coupling Level |
|--------|----------------|----------------|
| User | 106 | **Very High** (most coupled) |
| Concept | 93 | **Very High** |
| Patient | 72 | **High** |
| Person | 51 | **High** |
| Location | 48 | **High** |
| Order | 47 | **High** |
| Encounter | 43 | **Medium-High** |
| Obs | 32 | **Medium** |

## 2. Service-to-Domain Dependencies

### PatientService
**Direct Domain Dependencies**:
- Patient, PatientIdentifier, PatientIdentifierType
- Allergies, Allergy
- Concept, Location (cross-domain)

**Infrastructure Dependencies**:
- PatientDAO
- PersonMergeLogData
- IdentifierValidator (patient-specific)
- @Authorized annotation, PrivilegeConstants

### EncounterService
**Direct Domain Dependencies**:
- Encounter, EncounterRole, EncounterType
- Patient, Provider, Visit, VisitType (cross-domain)
- Location, Form, Cohort, User (cross-domain)

**Infrastructure Dependencies**:
- EncounterDAO
- EncounterVisitHandler
- EncounterSearchCriteria (parameter object)
- @Authorized annotation, PrivilegeConstants

### ObsService
**Direct Domain Dependencies**:
- Obs
- Concept, ConceptName (cross-domain)
- Encounter, Location, Person (cross-domain)

**Infrastructure Dependencies**:
- ObsDAO
- ComplexObsHandler
- @Authorized annotation, PrivilegeConstants

### OrderService
**Direct Domain Dependencies**:
- Order, OrderType, OrderFrequency, OrderGroup, CareSetting
- Concept, ConceptClass (cross-domain)
- Encounter, Patient, Provider (cross-domain)

**Infrastructure Dependencies**:
- OrderDAO
- @Authorized annotation, PrivilegeConstants

### ConceptService (Most Self-Contained)
**Direct Domain Dependencies** (all concept-scoped):
- Concept, ConceptAnswer, ConceptAttribute, ConceptAttributeType
- ConceptClass, ConceptComplex, ConceptDatatype, ConceptDescription
- ConceptMap, ConceptMapType, ConceptName, ConceptNameTag
- ConceptNumeric, ConceptProposal, ConceptReferenceTerm, ConceptReferenceTermMap
- ConceptSearchResult, ConceptSet, ConceptSource, ConceptStopWord
- Drug, DrugIngredient

**Infrastructure Dependencies**:
- ConceptDAO
- @Authorized annotation, PrivilegeConstants

### LocationService (Least Cross-Domain Coupling)
**Direct Domain Dependencies** (all location-scoped):
- Location, LocationAttribute, LocationAttributeType, LocationTag
- Address (shared)

**Infrastructure Dependencies**:
- LocationDAO
- @Authorized annotation, PrivilegeConstants

### PersonService
**Direct Domain Dependencies** (all person-scoped):
- Person, PersonName, PersonAddress, PersonAttribute, PersonAttributeType
- Relationship, RelationshipType

**Infrastructure Dependencies**:
- PersonDAO
- @Authorized annotation, PrivilegeConstants

### UserService
**Direct Domain Dependencies**:
- User, Role, Privilege
- Person (cross-domain - User has Person relationship)

**Infrastructure Dependencies**:
- UserDAO
- @Authorized annotation, PrivilegeConstants

## 3. Circular Dependency Identification

### Inheritance Coupling (High Risk)
- **Patient extends Person** - Strongest coupling; Patient IS-A Person. Breaking this requires a delegation/composition approach.

### Aggregate Coupling
- **Encounter** → references Patient, Obs (collection), Order (collection), Provider (collection), Visit, Location, Form
- **Visit** → references Patient, Encounter (collection), Location

### Cross-Domain Coupling
- **Obs** → Encounter, Concept, ConceptName, Person, Location
- **Order** → Encounter, Patient, Concept, ConceptClass, Provider
- **DrugOrder** → extends Order + Drug (Concept domain)
- **PatientIdentifier** → Patient, PatientIdentifierType, Location

### Service Locator Coupling (God Class)
- **Context.java** is used in **189 files** (29% of all Java files)
- Every service implementation, handler, and utility uses `Context.getXxxService()`
- This creates implicit runtime coupling between all modules

## 4. Infrastructure Coupling

### Database/ORM Layer
- All Hibernate DAO implementations depend on custom `DbSession`/`DbSessionFactory`
- Only 2 files directly reference DbSession (the interface and factory)
- All DAOs use Hibernate session through service-layer injection

### Spring Configuration
- **applicationContext-service.xml** (693 lines) wires everything through XML:
  - All service beans defined with AOP proxying
  - Authorization interceptors
  - Event listener registration
  - Single session factory bean

### Hibernate Configuration
- **hibernate.cfg.xml** registers all **84 HBM mapping files** in a single session factory
  - All entities share one session factory
  - Cross-entity queries span all domains
  - No session factory partitioning

## 5. HL7 Coupling Analysis

### ADTA28Handler (Patient Registration)
Cross-domain references:
- Patient, PatientIdentifier, PatientIdentifierType (patient domain)
- PersonName (person domain)
- Location (location domain)
- User (users-admin domain)
- Context.getPatientService(), Context.getLocationService(), Context.getAdministrationService()

### ORUR01Handler (Observation Results)
Cross-domain references (most coupled HL7 handler):
- **Patient domain**: Patient
- **Person domain**: Person, PersonAttribute, PersonAttributeType, Relationship, RelationshipType
- **Concept domain**: Concept, ConceptAnswer, ConceptName, ConceptProposal, Drug
- **Encounter domain**: Encounter, EncounterType, EncounterRole
- **Observation domain**: Obs, ComplexData
- **Location domain**: Location
- **Users domain**: User, Provider
- **Form domain**: Form
- Context.getConceptService(), Context.getEncounterService(), Context.getObsService(), etc.

**ORUR01Handler touches 8 domain areas** - it is the most coupled handler and represents the biggest challenge for module isolation.

## 6. Module Extraction Priority (Based on Coupling)

| Module | Coupling Score | Extraction Difficulty | Recommended Order |
|--------|---------------|----------------------|-------------------|
| Location | Low | Easy | 1st |
| Person | Low-Medium | Easy | 2nd |
| Concepts | Low (self-contained) | Easy | 3rd |
| Users-Admin | Medium | Medium | 4th |
| Patient | High (extends Person) | Hard | 5th |
| Encounter | High | Hard | 5th |
| Observation | Medium-High | Medium | 5th |
| Orders | Medium-High | Medium | 5th |
| Integration (HL7) | Very High (cross-all) | Very Hard | 6th |
| Search-Reporting | Medium | Medium | 7th |
| Web-Adapter | Medium | Medium | 8th |
| Shared-Kernel | Foundation | First (interfaces) | 0th |

## 7. Key Coupling Metrics Summary

| Metric | Value |
|--------|-------|
| Total Java files (api) | 649 |
| Files using Context.java | 189 (29%) |
| HBM mapping files | 84 |
| HBM files in single session factory | 84 (100%) |
| Spring XML bean definition lines | 693 |
| Cross-domain service dependencies | ~12 unique pairs |
| HL7 handlers cross-domain refs | ORUR01: 8 domains, ADTA28: 4 domains |
| Files referencing User entity | 106 |
| Files referencing Concept entity | 93 |
